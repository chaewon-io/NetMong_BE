package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Page<PostResponse> viewPostsByPage(Pageable pageable);
    Post uploadPost(PostRequest postRequest, Member foundMember, String foundUsername);
    PostResponse getDetail(long id);
    void deletePost(Long id, String foundUsername);
    void updatePost(Long id, PostRequest updatedPostRequest, String foundUsername);

    List<PostResponse> viewMyPosts(Long memberId);
}
