package com.ll.netmong.domain.reports.service;

import com.ll.netmong.domain.reports.dto.request.ReportRequest;
import com.ll.netmong.domain.reports.dto.response.ReportCommentResponse;
import com.ll.netmong.domain.reports.dto.response.ReportPostResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface ReportService {
    ReportPostResponse reportPost(ReportRequest reportRequest, Long postId, UserDetails userDetails);
    ReportCommentResponse reportComment(ReportRequest reportRequest, Long commentId, UserDetails userDetails);
}
