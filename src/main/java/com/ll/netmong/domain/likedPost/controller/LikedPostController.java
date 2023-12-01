package com.ll.netmong.domain.likedPost.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.likedPost.dto.request.LikedPostRequest;
import com.ll.netmong.domain.likedPost.dto.response.LikedPostResponse;
import com.ll.netmong.domain.likedPost.service.LikedPostService;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/likes")
public class LikedPostController {

    private final LikedPostService service;

    @PostMapping("/{postId}")
    public RsData<Void> addLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = service.getPostById(postId);
        service.addLike(post, userDetails);

        return RsData.successOf(null);
    }


    @GetMapping("/{postId}")
    public RsData<LikedPostResponse> getLikesCount(@PathVariable Long postId) {
        Post post = service.getPostById(postId);
        int likeCount = service.countLikes(post);

        return RsData.successOf(new LikedPostResponse(postId, likeCount));
    }

}
