package com.ll.netmong.domain.reports.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.reports.dto.response.ReportCommentResponse;
import com.ll.netmong.domain.reports.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reports.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reports")
public class ReportAdminController {

    private final ReportService reportService;

    @GetMapping("/posts")
    public RsData<List<ReportPostResponse>> getReportedPosts() {
        List<ReportPostResponse> reportedPosts = reportService.getReportedPosts();
        return RsData.successOf(reportedPosts);
    }

    @GetMapping("/comments")
    public RsData<List<ReportCommentResponse>> getReportedComments() {
        List<ReportCommentResponse> reportedComments = reportService.getReportedComments();
        return RsData.successOf(reportedComments);
    }
}
