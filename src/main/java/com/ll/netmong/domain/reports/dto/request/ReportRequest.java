package com.ll.netmong.domain.reports.dto.request;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.reports.util.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private String reporterUsername;
    private String content;
    private ReportType reportType;
}
