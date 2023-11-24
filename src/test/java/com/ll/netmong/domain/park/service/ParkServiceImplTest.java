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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공원이 존재하지 않습니다: " + parkId));

        when(parkRepository.findById(parkId)).thenReturn(Optional.of(existingPark));

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

}

