package com.ll.netmong.domain.reportPostComment.service;

import com.ll.netmong.common.ReportType;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.postComment.dto.response.ReportPostCommentResponse;

public interface ReportPostCommentService {
    ReportPostCommentResponse reportComment(Long id, Member member, ReportType reportType);
}
