package com.ll.netmong.domain.likedPost.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.likedPost.dto.response.LikedPostResponse;
import com.ll.netmong.domain.likedPost.service.LikedPostService;
import com.ll.netmong.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/likes")
public class LikedPostController {

    private final LikedPostService likedService;

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData addLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = likedService.getPostById(postId);
        likedService.addLike(post, userDetails);

        return RsData.successOf("좋아요를 추가하였습니다.");
    }

    @GetMapping("/{postId}")
    public RsData<LikedPostResponse> getLikesCount(@PathVariable Long postId) {
        Post post = likedService.getPostById(postId);
        Long likeCount = likedService.countLikes(post);

        return RsData.successOf(new LikedPostResponse(postId, likeCount));
    }

    @DeleteMapping("/{postId}")
    public RsData removeLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = likedService.getPostById(postId);
        likedService.removeLike(post, userDetails);

        return RsData.successOf("좋아요를 취소하였습니다.");
    }

}
