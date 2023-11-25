package com.ll.netmong.domain.park.service;

import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ParkServiceImplTest {

    @MockBean
    private ParkRepository parkRepository;

    @Autowired
    private ParkService parkService;

    private List<Park> sampleParks;

    @BeforeEach
    void setUp() {
        sampleParks = Arrays.asList(
                Park.builder()
                        .id(1L)
                        .parkNm("testPark1")
                        .lnmadr("Test Address 1")
                        .latitude(37.5665)
                        .longitude(126.9780)
                        .phoneNumber("010-1234-5678")
                        .state("Test State")
                        .city("Test City")
                        .build(),
                Park.builder()
                        .id(2L)
                        .parkNm("testPark2")
                        .lnmadr("Test Address 2")
                        .latitude(37.5665)
                        .longitude(126.9780)
                        .phoneNumber("010-1234-5678")
                        .state("Test State")
                        .city("Test City")
                        .build()
        );
    }

    @Test
    @DisplayName("getPark() 메서드는 유효한 parkId를 입력받으면, 해당 ParkResponse를 반환해야 한다.")
    void testGetParkExists() {
        Long parkId = 1L;
        Park existingPark = sampleParks.stream()
                .filter(park -> park.getId().equals(parkId))
                .findFirst()
                .orElse(null);

        assertNotNull(existingPark);

        when(parkRepository.findById(parkId)).thenReturn(Optional.ofNullable(existingPark));

        ParkResponse result = parkService.getPark(parkId);

        assertThat(result).isNotNull();
        assertThat(result.getParkNm()).isEqualTo(existingPark.getParkNm());
    }

    @Test
    @DisplayName("getPark() 메서드는 존재하지 않는 parkId를 입력받으면, IllegalArgumentException을 발생시켜야 한다.")
    void testGetParkNotExists() {
        Long parkId = 1L;

        when(parkRepository.findById(parkId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> parkService.getPark(parkId));
    }

    @Test
    @DisplayName("getParks() 메서드는 데이터를 조회하고, 조회한 데이터를 ParkResponse 객체로 변환한다.")
    void testGetParks() {
        when(parkRepository.findAll()).thenReturn(sampleParks);

        List<ParkResponse> result = parkService.getParks();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(sampleParks.size());
        assertThat(result.get(0).getParkNm()).isEqualTo(sampleParks.get(0).getParkNm());
        assertThat(result.get(1).getParkNm()).isEqualTo(sampleParks.get(1).getParkNm());
    }

    @Test   // Open API 호출과 데이터 저장 부분은 예외를 발생시키지 않는다고 가정한다.
    @DisplayName("getParks() 메서드는 비어있는 리스트를 반환하는 경우에도 예외를 발생시키지 않아야 한다.")
    void testGetParksWhenNoData() {
        when(parkRepository.findAll()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> parkService.getParks());
    }

    @Test
    @DisplayName("getStates() 메서드는 모든 유일한 state 목록을 반환해야 한다.")
    void testGetStates() {
        List<String> states = sampleParks.stream().map(Park::getState).distinct().collect(Collectors.toList());

        when(parkRepository.findStates()).thenReturn(states);

        List<String> result = parkService.getStates();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(states.size());
    }

    @Test
    @DisplayName("getCitiesByState() 메서드는 주어진 state에 해당하는 모든 유일한 city 목록을 반환해야 한다.")
    void testGetCitiesByState() {
        String state = "Test State";
        List<String> cities = sampleParks.stream()
                .filter(park -> park.getState().equals(state))
                .map(Park::getCity)
                .distinct()
                .collect(Collectors.toList());

        when(parkRepository.findCitiesByState(state)).thenReturn(cities);

        List<String> result = parkService.getCitiesByState(state);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(cities.size());
    }

    @Test
    @DisplayName("getParksByStateAndCity() 메서드는 유효한 state와 city를 입력받으면, 해당 지역의 ParkResponse 리스트를 반환해야 한다.")
    void testGetParksByStateAndCityExists() {

        String state = "Test State";
        String city = "Test City";

        List<Park> existingParks = sampleParks.stream()
                .filter(park -> park.getState().equals(state) && park.getCity().equals(city))
                .collect(Collectors.toList());

        when(parkRepository.findByLnmadrStartingWith(state + " " + city)).thenReturn(existingParks);

        List<ParkResponse> result = parkService.getParksByStateAndCity(state, city);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(existingParks.size());
        assertThat(result.get(0).getParkNm()).isEqualTo(existingParks.get(0).getParkNm());
        assertThat(result.get(1).getParkNm()).isEqualTo(existingParks.get(1).getParkNm());
    }

    @Test
    @DisplayName("getParksByStateAndCity() 메서드는 유효한 state와 city를 입력받았지만 해당 지역의 공원이 없는 경우, 비어있는 리스트를 반환해야 한다.")
    void testGetParksByStateAndCityNotExists() {
        String state = "Test State";
        String city = "Test City";

        when(parkRepository.findByLnmadrStartingWith(state + " " + city)).thenReturn(Collections.emptyList());

        List<ParkResponse> result = parkService.getParksByStateAndCity(state, city);

        assertThat(result).isEmpty();
    }

}

