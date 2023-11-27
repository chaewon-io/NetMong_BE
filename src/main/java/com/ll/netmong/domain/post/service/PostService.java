package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    List<PostResponse> getViewAll();
    void uploadPost(PostRequest postRequest, Member foundMember, String foundUsername);
    PostResponse getDetail(long id);
    void deletePost(long id, String foundUsername);
    void updatePost(long id, PostRequest updatedPostRequest, String foundUsername);
}
