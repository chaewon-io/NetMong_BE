package com.ll.netmong.domain.park.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkResponse {

    private String parkNm;
    private String lnmadr;
    private String phoneNumber;

    public ParkResponse(String parkNm, String lnmadr, String phoneNumber) {
        this.parkNm = parkNm;
        this.lnmadr = lnmadr;
        this.phoneNumber = phoneNumber;
    }
}