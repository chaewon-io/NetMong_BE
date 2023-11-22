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

    @GetMapping("/{id}")
    public RsData<ParkResponse> getPark(@PathVariable Long id) {
        ParkResponse park = parkService.getPark(id);
        return RsData.successOf(park);
    }

}