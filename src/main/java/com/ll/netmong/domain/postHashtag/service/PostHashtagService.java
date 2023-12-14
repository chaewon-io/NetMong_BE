package com.ll.netmong.domain.postHashtag.service;

import com.ll.netmong.domain.post.dto.request.PostRequest;

public interface PostHashtagService {
    void deleteHashtag(Long postId);
    void updateHashtag(Long postId, PostRequest updatedPostRequest);
}
