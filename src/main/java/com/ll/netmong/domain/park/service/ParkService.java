package com.ll.netmong.domain.park.service;

import com.ll.netmong.domain.park.dto.response.ParkResponse;

import java.util.List;

public interface ParkService {

    List<ParkResponse> getParks();

    ParkResponse getPark(Long id);

    List<ParkResponse> getParksByStateAndCity(String state, String city);

}