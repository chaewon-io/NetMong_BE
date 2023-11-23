package com.ll.netmong.domain.postComment.service;

import com.ll.netmong.common.DataNotFoundException;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;

import com.ll.netmong.post.Post;
import com.ll.netmong.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public PostComment addPostComment(Long postId, PostCommentRequest postCommentRequest) {
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
    public PostComment updateComment(Long commentId, PostCommentRequest request) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("해당 댓글이 없습니다. id=" + commentId));

        comment.update(request.getContent());

        return postCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("해당 댓글이 없습니다. id: " + commentId));
        comment.changeIsDeleted(true);
        postCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public List<PostComment> getCommentsOfPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("해당 게시글이 없습니다. id=" + postId));
        return post.getComments();
    }

    @Override
    @Transactional
    public PostComment addReplyToComment(Long commentId, PostCommentRequest request) {
        PostComment parentComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("해당 댓글이 없습니다. id: " + commentId));
        PostComment childComment = PostComment.builder()
                .content(request.getContent())
                .build();
        parentComment.addChildComment(childComment);
        return postCommentRepository.save(childComment);
    }

    @Override
    @Transactional
    public List<PostComment> getRepliesOfComment(Long commentId) {
        PostComment parentComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("해당 댓글이 없습니다. id: " + commentId));
        return parentComment.getChildComments();
    }

    @Override
    @Transactional
    public PostComment updateReply(Long replyId, PostCommentRequest request) {
        PostComment reply = postCommentRepository.findById(replyId)
                .orElseThrow(() -> new DataNotFoundException("해당 대댓글이 없습니다. id: " + replyId));
        reply.update(request.getContent());
        return postCommentRepository.save(reply);
    }
}
