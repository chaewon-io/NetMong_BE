package com.ll.netmong.domain.admin.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.reportPost.entity.ReportPost;
import com.ll.netmong.domain.reportPost.repository.ReportPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ReportPostRepository reportPostRepository;

    @GetMapping("/reports")
    public RsData<List<ReportPost>> getReportedPosts() {
        List<ReportPost> reports = reportPostRepository.findAll();
        return RsData.successOf(reports);
    }
}
