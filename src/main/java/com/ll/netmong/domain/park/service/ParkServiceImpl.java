package com.ll.netmong.domain.park.service;

import com.ll.netmong.common.RsData;
import com.ll.netmong.config.ApiKeys;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParkServiceImpl implements ParkService {

    private final ParkRepository parkRepository;
    private final ApiKeys apikeys;

    @Override
    public ParkResponse getPark(Long parkId) {
        Optional<Park> park = parkRepository.findById(parkId);
        if (park.isPresent()) {
            Park p = park.get();
            return new ParkResponse(p.getParkNm(), p.getLnmadr(), p.getLatitude(), p.getLongitude(), p.getPhoneNumber(), p.getState(), p.getCity());
        } else {
            throw new IllegalArgumentException("해당 ID의 공원이 존재하지 않습니다: " + parkId);
        }
    }

    @Override
    public List<ParkResponse> getParks() {
        List<Park> parks = parkRepository.findAll();

        if (parks.isEmpty()) {
            RsData<List<Park>> rsData = getParksFromApi();
            if (rsData.isSuccess()) {
                parks = rsData.getData();
                RsData<Void> saveResult = saveParks(parks);
                if (!saveResult.isSuccess()) {
                    throw new RuntimeException(saveResult.getMsg());
                }
            } else {
                throw new RuntimeException(rsData.getMsg());
            }
        }

        return parks.stream()
                .map(park -> new ParkResponse(park.getParkNm(), park.getLnmadr(), park.getLatitude(), park.getLongitude(), park.getPhoneNumber(), park.getState(), park.getCity()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkResponse> getParksByStateAndCity(String state, String city) {
        List<Park> parks = parkRepository.findByLnmadrStartingWith(state + " " + city);
        return parks.stream()
                .map(park -> new ParkResponse(park.getParkNm(), park.getLnmadr(), park.getLatitude(), park.getLongitude(), park.getPhoneNumber(), park.getState(), park.getCity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public RsData<List<Park>> getParksFromApi() {
        List<Park> parks = new ArrayList<>();
        int pageNo = 1;

        while (true) {
            String urlStr = "http://api.data.go.kr/openapi/tn_pubr_public_cty_park_info_api" +
                    "?ServiceKey=" + apikeys.getParkApiKey() +
                    "&pageNo=" + pageNo +
                    "&numOfRows=100";

            try {
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                String returnLine;
                StringBuilder result = new StringBuilder();

                while ((returnLine = br.readLine()) != null) {
                    result.append(returnLine).append("\n\r");
                }

                urlConnection.disconnect();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(result.toString())));

                NodeList nodeList = document.getElementsByTagName("item");

                if (nodeList.getLength() == 0) {
                    break;
                }

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);

                    String parkNm = element.getElementsByTagName("parkNm").item(0).getTextContent();
                    String lnmadr = element.getElementsByTagName("lnmadr").item(0).getTextContent();
                    String latitudeStr = element.getElementsByTagName("latitude").item(0).getTextContent();
                    double latitude = latitudeStr.isEmpty() ? 0 : Double.parseDouble(latitudeStr);
                    String longitudeStr = element.getElementsByTagName("longitude").item(0).getTextContent();
                    double longitude = longitudeStr.isEmpty() ? 0 : Double.parseDouble(longitudeStr);
                    String phoneNumber = element.getElementsByTagName("phoneNumber").item(0).getTextContent();
                    String[] lnmadrSplit = lnmadr.split("\\s");
                    String state = lnmadrSplit.length > 0 ? lnmadrSplit[0] : "";
                    String city = lnmadrSplit.length > 1 ? lnmadrSplit[1] : "";

                    Park park = Park.builder()
                            .parkNm(parkNm)
                            .lnmadr(lnmadr)
                            .latitude(latitude)
                            .longitude(longitude)
                            .phoneNumber(phoneNumber)
                            .state(state)
                            .city(city)
                            .build();

                    parks.add(park);
                }

                pageNo++;

            } catch (Exception e) {
                e.printStackTrace();
                return RsData.of("F-1", "공원 API 사용 중 다음과 같은 오류가 발생했습니다. 오류: " + e.getMessage());
            }
        }
        return RsData.of("S-1", "공원 API 성공", parks);
    }

    @Transactional
    public RsData<Void> saveParks(List<Park> parks) {
        try {
            parkRepository.saveAll(parks);
            return RsData.of("S-1", "공원 정보 저장 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return RsData.of("F-1", "공원 정보 저장 중 다음과 같은 오류가 발생했습니다. 오류: " + e.getMessage());
        }
    }

}