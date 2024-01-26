package com.ll.netmong.domain.park.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.service.ParkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parks")
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

    @GetMapping("/{parkId}")
    public RsData<ParkResponse> getPark(@PathVariable Long parkId, @AuthenticationPrincipal UserDetails userDetails) {
        ParkResponse park = parkService.getPark(parkId, userDetails);
        return RsData.successOf(park);
    }

    @GetMapping("/states")
    public RsData<List<String>> getStates() {
        List<String> states = parkService.getStates();
        return RsData.successOf(states);
    }

    @GetMapping("/states/{state}")
    public RsData<List<String>> getDistinctCitiesByState(@PathVariable String state) {
        List<String> cities = parkService.getCitiesByState(state);
        return RsData.successOf(cities);
    }

    @GetMapping("/states/{state}/{city}")
    public RsData<List<ParkResponse>> getParksByStateAndCity(@PathVariable String state, @PathVariable String city) {
        List<ParkResponse> parks = parkService.getParksByStateAndCity(state, city);
        return RsData.successOf(parks);
    }

    @GetMapping("/petAllowed")
    public RsData<List<ParkResponse>> getParksWithPetAllowed() {
        List<ParkResponse> parks = parkService.getParksWithPetAllowed();
        return RsData.successOf(parks);
    }
}
