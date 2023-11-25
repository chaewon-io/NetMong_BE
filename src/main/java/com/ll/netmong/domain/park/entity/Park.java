package com.ll.netmong.domain.park.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.parkComent.entity.ParkComent;
import com.ll.netmong.domain.parkLiked.entity.ParkLiked;
import jakarta.persistence.Column;
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

    @OneToMany(mappedBy = "park", fetch = LAZY)
    private List<ParkComent> parkComentList;

    @OneToMany(mappedBy = "park", fetch = LAZY)
    private List<ParkLiked> parkLiked;

    public ParkResponse toResponse() {
        return new ParkResponse(
                this.getParkNm(),
                this.getLnmadr(),
                this.getPhoneNumber()
        );
    }

}