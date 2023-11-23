package com.ll.netmong.domain.park.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkResponse {

    private String parkNm;
    private String lnmadr;
    private double latitude;
    private double longitude;
    private String phoneNumber;
    private String state;
    private String city;

    public ParkResponse(String parkNm, String lnmadr, double latitude, double longitude, String phoneNumber, String state, String city) {
        this.parkNm = parkNm;
        this.lnmadr = lnmadr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.city = city;
    }
}
