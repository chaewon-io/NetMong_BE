package com.ll.netmong.domain.likePark.service;

import com.ll.netmong.domain.park.dto.response.ParkResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface LikedParkService {
    void addLikeToPark(Long parkId, UserDetails userDetails);
    Long countLikesToPark(Long parkId);
    void removeLikeFromPark(Long parkId, UserDetails userDetails);
    List<ParkResponse> getLikedParksForMember(UserDetails userDetails);
}
