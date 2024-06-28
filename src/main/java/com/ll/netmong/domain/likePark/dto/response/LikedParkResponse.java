package com.ll.netmong.domain.likePark.dto.response;

import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.park.entity.Park;
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

    public static LikedPark of(Park park, Member member) {
        return LikedPark.builder()
                .park(park)
                .member(member)
                .build();
    }
}
