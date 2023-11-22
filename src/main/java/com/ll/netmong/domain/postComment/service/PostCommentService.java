package com.ll.netmong.domain.postComment.service;

import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;
import jakarta.transaction.Transactional;

public interface PostCommentService {

    void addPostComment(long postId, PostCommentRequest postCommentRequest);

    void modify(PostComment postComment, String content);

    void delete(PostComment postComment);

}
