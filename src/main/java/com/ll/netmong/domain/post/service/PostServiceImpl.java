package com.ll.netmong.domain.post.service;

import com.ll.netmong.common.PermissionDeniedException;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public List<PostResponse> getViewAll() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

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
    public void deletePost(long id, String foundUsername) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("포스트를 찾을 수 없습니다."));

        if (post.getWriter().equals(foundUsername)) {
            postRepository.deleteById(id);
        } else {
            throw new PermissionDeniedException("해당 포스트에 대한 삭제 권한이 없습니다.");
        }
    }

    @Override
    @Transactional
    public void updatePost(long id, PostRequest updatedPostRequest, String foundUsername) {
        Post originPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("포스트를 찾을 수 없습니다."));

        if (originPost.getWriter().equals(foundUsername)) {
            Post updatedPost = originPost.toBuilder()
                    .title(updatedPostRequest.getTitle())
                    .content(updatedPostRequest.getContent())
                    .imageUrl(updatedPostRequest.getImageUrl()).build();

            postRepository.save(updatedPost);
        } else {
            throw new PermissionDeniedException("해당 포스트에 대한 수정 권한이 없습니다.");
        }
    }
}
