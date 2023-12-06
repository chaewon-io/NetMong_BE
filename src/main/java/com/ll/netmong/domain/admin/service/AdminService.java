package com.ll.netmong.domain.admin.service;

import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;

import java.util.List;

public interface AdminService {
    List<ReportPostResponse> getAllReportPosts();
}
