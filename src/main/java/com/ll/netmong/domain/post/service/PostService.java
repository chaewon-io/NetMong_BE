package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;

public interface PostService {
    void uploadPost(PostRequest postRequest, Member foundMember, String foundUsername);
    PostResponse getDetail(long id);
    void deletePost(long id);
    void updatePost(long id, PostRequest updatedPostRequest);
}
