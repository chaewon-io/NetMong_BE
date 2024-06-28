package com.ll.netmong.domain.likePark.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.likePark.dto.response.LikedParkResponse;
import com.ll.netmong.domain.likePark.service.LikedParkService;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parks/likes")
public class LikedParkController {
    private final LikedParkService likedParkService;

    @PostMapping("/{parkId}")
    public RsData addLikeToPark(@PathVariable Long parkId, @AuthenticationPrincipal UserDetails userDetails) {
        likedParkService.addLikeToPark(parkId, userDetails);
        return RsData.successOf("좋아요를 추가하였습니다.");
    }

    @GetMapping("/{parkId}")
    public RsData<LikedParkResponse> getCountLikesByPark(@PathVariable Long parkId) {
        Long likeCount = likedParkService.countLikesToPark(parkId);
        return RsData.successOf(new LikedParkResponse(parkId, likeCount));
    }

    @DeleteMapping("/{parkId}")
    public RsData removeLikeFromPark(@PathVariable Long parkId, @AuthenticationPrincipal UserDetails userDetails) {
        likedParkService.removeLikeFromPark(parkId, userDetails);
        return RsData.successOf("좋아요를 취소하였습니다.");
    }

    @GetMapping
    public RsData<List<ParkResponse>> getLikedParksByMember(@AuthenticationPrincipal UserDetails userDetails) {
        List<ParkResponse> parkResponses = likedParkService.getLikedParksForMember(userDetails);
        return RsData.successOf(parkResponses);
    }

}
