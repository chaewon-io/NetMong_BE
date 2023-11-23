package com.ll.netmong.domain.park.service;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;

import java.util.List;

public interface ParkService {

    List<ParkResponse> getParks();

    ParkResponse getPark(Long parkId);

    List<ParkResponse> getParksByStateAndCity(String state, String city);

    RsData<List<Park>> getParksFromApi();

    RsData<Void> saveParks(List<Park> parks);

}