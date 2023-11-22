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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public PostComment addPostComment(long postId, PostCommentRequest postCommentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 게시물을 찾을 수 없습니다."));
        PostComment comment = PostComment.builder()
                .post(post)
                .content(postCommentRequest.getContent())
                .build();
        post.getComments().add(comment);
        return postCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public PostComment updateComment(Long id, PostCommentRequest request) {
        PostComment comment = postCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        comment.update(request.getContent());

        return postCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        postCommentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<PostComment> getCommentsOfPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
        return post.getComments();
    }
}
