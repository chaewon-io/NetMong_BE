package com.ll.netmong.domain.likePark.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.park.entity.Park;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikedPark extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static LikedPark createLikedPark(Park park, Member member) {
        return LikedPark.builder()
                .park(park)
                .member(member)
                .build();
    }
}
