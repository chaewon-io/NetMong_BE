package com.ll.netmong.domain.postComment.service;

import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;

import java.util.List;

public interface PostCommentService {

    PostComment addPostComment(Long postId, PostCommentRequest postCommentRequest);

    PostComment updateComment(Long commentId, PostCommentRequest request);

    void deleteComment(Long commentId);

    List<PostComment> getCommentsOfPost(Long postId);

    PostComment addReplyToComment(Long commentId, PostCommentRequest request);

    List<PostComment> getRepliesOfComment(Long commentId);

    PostComment updateReply(Long replyId, PostCommentRequest request);

}
