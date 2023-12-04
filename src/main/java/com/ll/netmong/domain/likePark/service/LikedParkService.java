package com.ll.netmong.domain.likePark.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.park.entity.Park;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface LikedParkService {
    void addLikeToPark(Park park, UserDetails userDetails);

    Park getParkById(Long parkId);

    Member getMemberById(@AuthenticationPrincipal UserDetails userDetails);
}
