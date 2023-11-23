package com.ll.netmong.domain.postComment.service;

import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;

import java.util.List;

public interface PostCommentService {

    PostComment addPostComment(Long postId, PostCommentRequest postCommentRequest);

    PostComment updateComment(Long id, PostCommentRequest request);

    void deleteComment(Long id);

    List<PostComment> getCommentsOfPost(Long postId);

}
