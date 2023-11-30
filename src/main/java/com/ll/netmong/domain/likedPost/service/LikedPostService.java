package com.ll.netmong.domain.likedPost.service;

import com.ll.netmong.domain.likedPost.dto.request.LikedPostRequest;
import com.ll.netmong.domain.likedPost.dto.response.LikedPostResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface LikedPostService {

    LikedPostResponse addLike(Long postId, LikedPostRequest likedPostRequest, UserDetails userDetails);
}
