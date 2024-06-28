package com.ll.netmong.domain.park.dto.response;

import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import com.ll.netmong.domain.parkComment.entity.ParkComment;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkResponse {

    private Long id;
    private String parkNm;
    private String lnmadr;
    private double latitude;
    private double longitude;
    private String phoneNumber;
    private String state;
    private String city;
    private Long likesCount;
    private Boolean isLiked;
    private Boolean petAllowed;

    public static ParkResponse of(Park park, Boolean isLiked) {
        return ParkResponse.builder()
                .id(park.getId())
                .parkNm(park.getParkNm())
                .lnmadr(park.getLnmadr())
                .latitude(park.getLatitude())
                .longitude(park.getLongitude())
                .phoneNumber(park.getPhoneNumber())
                .state(park.getState())
                .city(park.getCity())
                .likesCount(park.getLikesCount())
                .isLiked(isLiked)
                .petAllowed(park.getPetAllowed())
                .build();
    }

}
