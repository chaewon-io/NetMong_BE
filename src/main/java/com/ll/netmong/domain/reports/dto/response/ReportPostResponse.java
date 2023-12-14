package com.ll.netmong.domain.reports.dto.response;


import com.ll.netmong.domain.reports.entity.ReportPost;
import com.ll.netmong.domain.reports.util.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportPostResponse {
    private ReportType reportType;
    private String content;
    private Long reportedPostId;

    public ReportPostResponse(ReportPost reportPost) {
        this.content = reportPost.getContent();
        this.reportType = reportPost.getReportType();
        this.reportedPostId = reportPost.getReported().getId();
    }
}
