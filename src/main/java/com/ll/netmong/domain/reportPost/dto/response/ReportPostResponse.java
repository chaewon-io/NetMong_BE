package com.ll.netmong.domain.reportPost.dto.response;


import com.ll.netmong.common.ReportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPostResponse {
    private Long reportId; // 신고 Id
    private Long reporterId; // 신고한 유저
    private Long reportedPostId; // 신고된 게시글
    private ReportType reportType; // 신고 유형
    private String content; // 신고 사유

    public ReportPostResponse(Long reportId, Long reporterId, Long reportedPostId, ReportType reportType, String content) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reportedPostId = reportedPostId;
        this.reportType = reportType;
        this.content = content;
    }

}
