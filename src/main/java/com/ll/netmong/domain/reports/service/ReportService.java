package com.ll.netmong.domain.reports.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.reports.dto.request.ReportRequest;
import com.ll.netmong.domain.reports.dto.response.ReportCommentResponse;
import com.ll.netmong.domain.reports.dto.response.ReportPostResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ReportService {
    ReportPostResponse reportPost(ReportRequest reportRequest, Post reportedPost, Member reporter);
    ReportCommentResponse reportComment(ReportRequest reportRequest, PostComment reportedComment, Member reporter);
    List<ReportPostResponse> getReportedPosts();
    List<ReportCommentResponse> getReportedComments();
}
