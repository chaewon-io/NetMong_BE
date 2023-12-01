package com.ll.netmong.domain.likedPost.service;

import com.ll.netmong.domain.likedPost.dto.request.LikedPostRequest;
import com.ll.netmong.domain.likedPost.dto.response.LikedPostResponse;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface LikedPostService {

    void addLike(Post post, UserDetails userDetails);
    void removeLike(Post post, @AuthenticationPrincipal UserDetails userDetails);
    int countLikes(Post post);
    Post getPostById(Long postId);
    Member getMemberById(@AuthenticationPrincipal UserDetails userDetails);
}
