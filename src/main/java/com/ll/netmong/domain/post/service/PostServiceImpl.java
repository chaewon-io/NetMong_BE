package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void uploadPost(PostRequest postRequest) {
        postRepository.save(Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent()).build());
    }

    @Override
    @Transactional
    public PostResponse getDetail(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);;

        return new PostResponse(post);
    }
}
