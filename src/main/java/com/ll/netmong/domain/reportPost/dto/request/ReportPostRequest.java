package com.ll.netmong.domain.reportPost.dto.request;

import com.ll.netmong.common.ReportType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPostRequest {
    private ReportType reportType;

    @NotBlank(message = "신고 사유를 입력하세요.")
    private String content;
}
