package com.ll.netmong.domain.park.service;

import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class ParkServiceImplTest {

    @Autowired
    private ParkService parkService;

    @Autowired
    private ParkRepository parkRepository;

    @BeforeEach
    void setUp() {

        String state = "State1";
        String city = "City1";
        List<Park> existingParks = parkRepository.findByLnmadrStartingWith(state + " " + city);

        if (!existingParks.isEmpty()) {
            parkRepository.deleteAll(existingParks);
        }

        Park mockPark = Park.builder()
                .id(0L)
                .parkNm("testPark1")
                .lnmadr("State1 City1 Park1")
                .latitude(37.1234)
                .longitude(127.5678)
                .phoneNumber("123-4567")
                .state(state)
                .city(city)
                .build();

        parkRepository.save(mockPark);
    }

    @Test
    @DisplayName("getParks() 메서드는 Open Api에서 데이터를 조회하여 저장한 뒤 반환한다.")
    void testGetParks() {

        List<ParkResponse> result = parkService.getParks();

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("getPark(Long id) 메서드는 해당 공원 Id를 조회한다.")
    void testGetPark() {

        Long parkId = 0L;

        assertThatThrownBy(() -> parkService.getPark(parkId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 ID의 공원이 존재하지 않습니다: " + parkId);
    }

    @Test
    @DisplayName("getParksByStateAndCity(String state, String city) 메서드는 시도/구군을 기준으로 공원 목록을 조회한다.")
    void testGetParksByStateAndCity() {

        String state = "State1";
        String city = "City1";

        List<ParkResponse> result = parkService.getParksByStateAndCity(state, city);

        assertThat(result).isNotEmpty();
    }

}