package com.ll.netmong.domain.parkComent.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.park.entity.Park;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ParkComent extends BaseEntity {

    private String content;

    @ManyToOne(fetch = LAZY)
    private Park park;

    @ManyToOne(fetch = LAZY)
    private Member member;

}
