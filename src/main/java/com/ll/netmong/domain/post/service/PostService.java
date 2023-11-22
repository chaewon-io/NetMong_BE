package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;

public interface PostService {
    void uploadPost(PostRequest postRequest);
    PostResponse getDetail(long id);
}
