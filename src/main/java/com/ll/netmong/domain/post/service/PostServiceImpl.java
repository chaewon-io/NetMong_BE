package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public void uploadPost(PostRequest postRequest, Member foundMember, String foundUsername) {
        postRepository.save(Post.builder()
                .title(postRequest.getTitle())
                .member(foundMember)
                .writer(foundUsername)
                .content(postRequest.getContent())
                .imageUrl(postRequest.getImageUrl()).build());
    }

    @Override
    public PostResponse getDetail(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("포스트를 찾을 수 없습니다."));

        return new PostResponse(post);
    }

    @Override
    @Transactional
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePost(long id, PostRequest updatedPostRequest) {
        Post originPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("포스트를 찾을 수 없습니다."));

        Post updatedPost = originPost.toBuilder()
                .title(updatedPostRequest.getTitle())
                .content(updatedPostRequest.getContent())
                .build();

        postRepository.save(updatedPost);
    }
}
