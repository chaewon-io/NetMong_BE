package com.ll.netmong.domain.park.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.service.ParkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parks")
public class ParkController {

    private final ParkService parkService;

    @GetMapping
    public RsData<List<ParkResponse>> getParks() {
        List<ParkResponse> parks = parkService.getParks();
        return RsData.successOf(parks);
    }

    @PostMapping
    public RsData<String> saveParksFromApi() {
        parkService.saveParksFromApi();
        return RsData.successOf("공원 정보가 성공적으로 저장되었습니다.");
    }

    // Id로 조회 필요 없을 시 삭제 예정
    @GetMapping("/{parkId}")
    public RsData<ParkResponse> getPark(@PathVariable Long parkId) {
        ParkResponse park = parkService.getPark(parkId);
        return RsData.successOf(park);
    }

    @GetMapping("/{state}/{city}")
    public RsData<List<ParkResponse>> getParksByStateAndCity(@PathVariable String state, @PathVariable String city) {
        List<ParkResponse> parks = parkService.getParksByStateAndCity(state, city);
        return RsData.successOf(parks);
    }

}