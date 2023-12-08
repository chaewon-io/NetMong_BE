package com.ll.netmong.domain.admin.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.admin.dto.reponse.AdminReportPostCommentResponse;
import com.ll.netmong.domain.admin.service.AdminService;
import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/reports/posts")
    public RsData<List<ReportPostResponse>> getReportedPosts() {
        List<ReportPostResponse> reports = adminService.getAllReportPosts();
        return RsData.successOf(reports);
    }

    @GetMapping("/reports/comments")
    public RsData<List<AdminReportPostCommentResponse>> getReportedCommentsDetails() {
        List<AdminReportPostCommentResponse> reports = adminService.getAllReportedComments();
        return RsData.successOf(reports);
    }

}
