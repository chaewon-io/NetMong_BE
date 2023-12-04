package com.ll.netmong.domain.follow.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Follow extends BaseEntity {

    @ManyToOne
    private Member followers;

    @ManyToOne
    private Member followings;
}
