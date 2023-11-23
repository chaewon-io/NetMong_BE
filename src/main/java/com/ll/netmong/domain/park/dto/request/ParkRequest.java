package com.ll.netmong.domain.park.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkRequest {

    private String parkNm;
    private String lnmadr;
    private double latitude;
    private double longitude;
    private String phoneNumber;
    private String state;
    private String city;

}
