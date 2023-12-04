package com.ll.netmong.domain.likePark.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.likePark.service.LikedParkService;
import com.ll.netmong.domain.park.entity.Park;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parks/likes")
public class LikedParkController {
    private final LikedParkService likedParkService;

    @PostMapping("/{parkId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData addLikeToPark(@PathVariable Long parkId, @AuthenticationPrincipal UserDetails userDetails) {
        Park park = likedParkService.getParkById(parkId);
        likedParkService.addLikeToPark(park, userDetails);

        return RsData.successOf("좋아요를 추가하였습니다.");
    }
}
