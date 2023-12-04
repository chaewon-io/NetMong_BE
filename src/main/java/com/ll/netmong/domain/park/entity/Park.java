package com.ll.netmong.domain.park.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.parkComment.entity.ParkComment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Park extends BaseEntity {

    @Column(name = "parkNm")
    private String parkNm;

    @Column(name = "lnmadr")
    private String lnmadr;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL)
    private List<ParkComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL)
    private List<LikedPark> likedParks = new ArrayList<>();

    public ParkResponse toResponse() {
        return ParkResponse.builder()
                .id(getId())
                .parkNm(parkNm)
                .lnmadr(lnmadr)
                .latitude(latitude)
                .longitude(longitude)
                .phoneNumber(phoneNumber)
                .state(state)
                .city(city)
                .likesCount(likesCount)
                .build();
    }

    public void addComment(ParkComment comment) {
        this.comments.add(comment);
        comment.setPark(this);
    }

    @Builder.Default
    @Column(name = "likes_count", nullable = false)
    private Long likesCount = 0L;

    public void addLikeToPark(LikedPark like) {
        this.likedParks.add(like);
        like.setPark(this);
        this.likesCount++;
    }

    public void removeLikeFromPark(LikedPark like) {
        this.likedParks.remove(like);
        like.setPark(null);
        this.likesCount--;
    }
}
