package com.ll.netmong.domain.likePark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedParkResponse {
    private Long parkId;
    private Long likedCount;
}
