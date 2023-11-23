package com.ll.netmong.domain.post.service;

import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void uploadPost(MultipartFile image, PostRequest postRequest) {
        postRepository.save(Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .imageUrl(postRequest.getImageUrl()).build());
    }

    @Override
    @Transactional
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

        originPost.setTitle(updatedPostRequest.getTitle());
        originPost.setContent(updatedPostRequest.getContent());

        postRepository.save(originPost);
    }
}
