package com.ll.netmong.domain.park.service;

import com.ll.netmong.domain.park.dto.response.ParkResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ParkService {
    List<ParkResponse> getParks();

    void saveParksFromApi();

    ParkResponse getPark(Long parkId, UserDetails userDetails);

    List<String> getStates();

    List<String> getCitiesByState(String state);

    List<ParkResponse> getParksByStateAndCity(String state, String city);
}
