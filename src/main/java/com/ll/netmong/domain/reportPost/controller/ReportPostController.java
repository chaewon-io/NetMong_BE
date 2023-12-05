package com.ll.netmong.domain.reportPost.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.reportPost.dto.request.ReportPostRequest;
import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reportPost.service.ReportPostService;
import com.ll.netmong.domain.reportPost.util.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/reports")
public class ReportPostController {

    private final ReportPostService service;

    @PostMapping("/{postId}")
    public RsData<ReportPostResponse> reportPost(@PathVariable Long postId, @RequestBody ReportPostRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        ReportPostResponse response = service.reportPost(postId, request, userDetails);
        return RsData.successOf(response);
    }

    @GetMapping("/types")
    public RsData<List<String>> getReportTypes() {
        List<String> reportTypes = Arrays.stream(ReportType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return RsData.successOf(reportTypes);
    }
}
