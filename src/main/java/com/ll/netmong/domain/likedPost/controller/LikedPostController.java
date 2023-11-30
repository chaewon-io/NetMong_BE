package com.ll.netmong.domain.likedPost.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.likedPost.dto.request.LikedPostRequest;
import com.ll.netmong.domain.likedPost.dto.response.LikedPostResponse;
import com.ll.netmong.domain.likedPost.service.LikedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/likes")
public class LikedPostController {

    private final LikedPostService service;

    @PostMapping("/{postId}")
    public RsData<LikedPostResponse> addLike(@PathVariable Long postId, LikedPostRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        LikedPostResponse newLike = service.addLike(postId, request, userDetails);
        return RsData.successOf(newLike);
    }
}
