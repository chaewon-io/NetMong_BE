package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.post.dto.request.PostRequest;

public interface PostService {
    void uploadPost(PostRequest postRequest);
}
