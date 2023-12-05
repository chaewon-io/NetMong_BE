package com.ll.netmong.domain.reportPost.service;

import com.ll.netmong.domain.reportPost.dto.request.ReportPostRequest;
import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface ReportPostService {
    ReportPostResponse reportPost(Long postId, ReportPostRequest request, @AuthenticationPrincipal UserDetails userDetails);

}
