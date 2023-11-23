package com.ll.netmong.domain.member.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.parkComent.entity.ParkComent;
import com.ll.netmong.domain.parkLiked.entity.ParkLiked;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Member extends BaseEntity {

    private String username;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<ParkComent> walkComentList;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<ParkLiked> walkLiked;

}