package com.ll.netmong.domain.reports.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.reports.dto.request.ReportRequest;
import com.ll.netmong.domain.reports.dto.response.ReportCommentResponse;
import com.ll.netmong.domain.reports.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reports.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/post/{postId}")
    public RsData<ReportPostResponse> reportPost(@RequestBody ReportRequest reportRequest, @PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        ReportPostResponse reportPostResponse = reportService.reportPost(reportRequest, postId, userDetails);
        return RsData.successOf(reportPostResponse);
    }

    @PostMapping("/comment/{commentId}")
    public RsData<ReportCommentResponse> reportComment(@RequestBody ReportRequest reportRequest, @PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        ReportCommentResponse reportCommentResponse = reportService.reportComment(reportRequest, commentId, userDetails);
        return RsData.successOf(reportCommentResponse);
    }
}
