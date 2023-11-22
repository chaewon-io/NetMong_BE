package com.ll.netmong.domain.park.dto.response;

import lombok.*;

@Getter
@Setter
public class ParkResponse {

    private String parkNm;
    private String lnmadr;
    private double latitude;
    private double longitude;
    private String phoneNumber;

    public ParkResponse(String parkNm, String lnmadr, double latitude, double longitude, String phoneNumber) {
        this.parkNm = parkNm;
        this.lnmadr = lnmadr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
    }

}