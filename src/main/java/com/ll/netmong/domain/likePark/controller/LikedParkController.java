package com.ll.netmong.domain.likePark.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.likePark.dto.response.LikedParkResponse;
import com.ll.netmong.domain.likePark.service.LikedParkService;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parks/likes")
public class LikedParkController {
    private final LikedParkService likedParkService;

    @PostMapping("/{parkId}")
    public RsData addLikeToPark(@PathVariable Long parkId, @AuthenticationPrincipal UserDetails userDetails) {
        Park park = likedParkService.getParkById(parkId);
        likedParkService.addLikeToPark(park, userDetails);

        return RsData.successOf("좋아요를 추가하였습니다.");
    }

    @GetMapping("/{parkId}")
    public RsData<LikedParkResponse> getCountLikesByPark(@PathVariable Long parkId) {
        Park park = likedParkService.getParkById(parkId);
        Long likeCount = likedParkService.countLikesToPark(park);

        return RsData.successOf(new LikedParkResponse(parkId, likeCount));
    }

    @DeleteMapping("/{parkId}")
    public RsData removeLikeFromPark(@PathVariable Long parkId, @AuthenticationPrincipal UserDetails userDetails) {
        Park park = likedParkService.getParkById(parkId);
        likedParkService.removeLikeFromPark(park, userDetails);

        return RsData.successOf("좋아요를 취소하였습니다.");
    }

    @GetMapping
    public RsData<List<ParkResponse>> getLikedParksByUser(@AuthenticationPrincipal UserDetails userDetails) {
        List<Park> likedParks = likedParkService.getLikedParksByUser(userDetails);
        List<ParkResponse> parkResponses = likedParks.stream()
                .map(Park::toResponse)
                .collect(Collectors.toList());

        return RsData.successOf(parkResponses);
    }
}
