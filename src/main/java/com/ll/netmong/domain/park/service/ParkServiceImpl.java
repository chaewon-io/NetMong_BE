package com.ll.netmong.domain.park.service;

import com.ll.netmong.base.config.ApiKeys;
import com.ll.netmong.domain.likePark.repository.LikedParkRepository;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.exception.MemberNotFoundException;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.exception.ParkNotFoundException;
import com.ll.netmong.domain.park.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkServiceImpl implements ParkService {

    private final ParkRepository parkRepository;
    private final ApiKeys apikeys;
    private final LikedParkRepository likedParkRepository;
    private final MemberRepository memberRepository;

// 실제 배포 시 활성화
//    @PostConstruct
//    public void init() {
//        if (parkRepository.count() == 0) {
//            saveParksFromApi();
//        }
//    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkResponse> getParks() {
        List<Park> parks = parkRepository.findAll();
        return parks.stream()
                .map(park -> ParkResponse.of(park, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveParksFromApi() {
        int pageNo = 1;
        List<Park> parks = new ArrayList<>();

        while (true) {
            String result = callApi(pageNo);
            List<Park> newParks = parseParksData(result);
            if (newParks.isEmpty()) {
                break;
            }
            parks.addAll(newParks);
            pageNo++;
        }

        parkRepository.saveAll(parks);
    }

    @Override
    @Transactional(readOnly = true)
    public ParkResponse getPark(Long parkId, UserDetails userDetails) {
        Park park = getParkById(parkId);
        Member member = getMemberById(userDetails);
        boolean isLiked = likedParkRepository.existsByMemberAndPark(member, park);
        return ParkResponse.of(park, isLiked);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getStates() {
        return parkRepository.findStates();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCitiesByState(String state) {
        return parkRepository.findCitiesByState(state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkResponse> getParksByStateAndCity(String state, String city, UserDetails userDetails) {
        List<Park> parks = parkRepository.findByLnmadrStartingWith(state + " " + city);
        Member member = getMemberById(userDetails);
        List<Long> likedParkIds = likedParkRepository.findLikedParkIdsByMemberId(member.getId());

        return parks.stream()
                .map(park -> ParkResponse.of(park, likedParkIds.contains(park.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkResponse> getParksWithPetAllowed() {
        List<Park> parks = parkRepository.findByPetAllowedTrue();
        return parks.stream()
                .map(park -> ParkResponse.of(park, null))
                .collect(Collectors.toList());
    }

    private String callApi(int pageNo) {
        String urlStr = "http://api.data.go.kr/openapi/tn_pubr_public_cty_park_info_api" +
                "?ServiceKey=" + apikeys.getParkApiKey() +
                "&pageNo=" + pageNo +
                "&numOfRows=100";
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String returnLine;

            while ((returnLine = br.readLine()) != null) {
                result.append(returnLine).append("\n\r");
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result.toString();
    }

    private List<Park> parseParksData(String data) {
        List<Park> parks = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList nodeList = document.getElementsByTagName("item");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                parks.add(createParkFromElement(element));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parks;
    }

    private Park createParkFromElement(Element element) {
        String parkNm = element.getElementsByTagName("parkNm").item(0).getTextContent();
        String lnmadr = element.getElementsByTagName("lnmadr").item(0).getTextContent();
        double latitude = Double.parseDouble(getElementTextContent(element, "latitude", "0"));
        double longitude = Double.parseDouble(getElementTextContent(element, "longitude", "0"));
        String phoneNumber = element.getElementsByTagName("phoneNumber").item(0).getTextContent();
        String[] lnmadrSplit = lnmadr.split("\\s");
        String state = lnmadrSplit.length > 0 ? lnmadrSplit[0] : "";
        String city = lnmadrSplit.length > 1 ? lnmadrSplit[1] : "";

        return Park.builder()
                .parkNm(parkNm)
                .lnmadr(lnmadr)
                .latitude(latitude)
                .longitude(longitude)
                .phoneNumber(phoneNumber)
                .state(state)
                .city(city)
                .build();
    }

    private String getElementTextContent(Element element, String tagName, String defaultValue) {
        Node node = element.getElementsByTagName(tagName).item(0);
        return node != null ? node.getTextContent() : defaultValue;
    }

    private Member getMemberById(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Park getParkById(Long parkId) {
        return parkRepository.findById(parkId)
                .orElseThrow(() -> new ParkNotFoundException("해당하는 공원을 찾을 수 없습니다." + parkId));
    }

}
