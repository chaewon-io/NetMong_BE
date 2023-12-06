package com.ll.netmong.domain.likePark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedParkRequest {
    private Long parkId;
    private Long memberId;
}
