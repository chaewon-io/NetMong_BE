package com.ll.netmong.domain.postComment.service;

import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.dto.response.PostCommentResponse;
import com.ll.netmong.domain.postComment.entity.PostComment;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostCommentService {

    PostCommentResponse addPostComment(Long postId, PostCommentRequest postCommentRequest, UserDetails userDetails);

    PostComment updateComment(Long commentId, PostCommentRequest request);

    void deleteComment(Long commentId);

    List<PostComment> getCommentsOfPost(Long postId);

    PostComment addReplyToComment(Long commentId, PostCommentRequest request, UserDetails userDetails);

    List<PostComment> getRepliesOfComment(Long commentId);

    PostComment updateReply(Long replyId, PostCommentRequest request);

}