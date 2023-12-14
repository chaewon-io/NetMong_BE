package com.ll.netmong.domain.reports.dto.response;

import com.ll.netmong.domain.reports.entity.ReportComment;
import com.ll.netmong.domain.reports.util.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCommentResponse {
    private ReportType reportType;
    private Long reportedPostId;
    private Integer reportCount;
    private Boolean isBlinded;
    private String content;

    public ReportCommentResponse(ReportComment reportComment) {
        this.reportType = reportComment.getReportType();
        this.reportedPostId = reportComment.getReported().getPost().getId();
        this.reportCount = reportComment.getReported().getReportCount();
        this.isBlinded = reportComment.getReported().getIsBlinded();
        this.content = reportComment.getContent();
    }
}
