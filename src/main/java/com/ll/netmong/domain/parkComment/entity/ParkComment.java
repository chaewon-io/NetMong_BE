package com.ll.netmong.domain.parkComment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.park.entity.Park;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParkComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    @JsonIgnore
    private Park park;

    @Column(length = 500)
    private String content;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(name = "member_id")
    private Long memberId;

    private String username;

    public static ParkComment toEntity(Park park, Member member, String content) {
        return ParkComment.builder()
                .park(park)
                .memberId(member.getId())
                .username(member.getUsername())
                .content(content)
                .isDeleted(false)
                .build();
    }

    public void setPark(Park park) { this.park = park; }
    public void updateContent(String content) {
        this.content = content;
    }
    public void markAsDeleted(Boolean b) {
        this.isDeleted = b;
    }

}