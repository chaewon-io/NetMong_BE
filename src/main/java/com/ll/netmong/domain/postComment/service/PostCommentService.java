package com.ll.netmong.domain.postComment.service;

import com.ll.netmong.common.ReportType;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.dto.response.PostCommentResponse;
import com.ll.netmong.domain.postComment.dto.response.ReportPostCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostCommentService {

    PostCommentResponse addPostComment(Long postId, PostCommentRequest postCommentRequest, UserDetails userDetails);

    PostCommentResponse updateComment(Long commentId, PostCommentRequest request, UserDetails userDetails);

    void deleteComment(Long commentId, UserDetails userDetails);

    Page<PostCommentResponse> getCommentsOfPost(Long postId, Pageable pageable);

    PostCommentResponse addReplyToComment(Long commentId, PostCommentRequest request, UserDetails userDetails);

    PostCommentResponse updateReply(Long replyId, PostCommentRequest request);

    ReportPostCommentResponse reportComment(Long id, Member member, ReportType reportType);

}
