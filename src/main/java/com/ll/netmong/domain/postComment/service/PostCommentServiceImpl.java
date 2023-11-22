package com.ll.netmong.domain.postComment.service;

import com.ll.netmong.common.DataNotFoundException;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;

import com.ll.netmong.post.Post;
import com.ll.netmong.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void addPostComment(long postId, PostCommentRequest postCommentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 게시물을 찾을 수 없습니다."));
        PostComment comment = PostComment.builder()
                .post(post)
                .content(postCommentRequest.getContent())
                .build();
        post.getComments().add(comment);
        postCommentRepository.save(comment);
    }

    @Override
    public void modify(PostComment postComment, String content) {
        postComment.setContent(content);
        postCommentRepository.save(postComment);
    }

    @Override
    public void delete(PostComment postComment) {
        postCommentRepository.delete(postComment);
    }
}
