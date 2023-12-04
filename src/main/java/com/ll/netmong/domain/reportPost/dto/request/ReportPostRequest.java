package com.ll.netmong.domain.reportPost.dto.request;

import com.ll.netmong.domain.reportPost.util.ReportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPostRequest {
    private ReportType reportType;
    private String reportContent;
}
