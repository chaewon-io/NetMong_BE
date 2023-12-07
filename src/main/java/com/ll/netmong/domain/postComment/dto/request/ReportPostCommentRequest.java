package com.ll.netmong.domain.postComment.dto.request;

import com.ll.netmong.common.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportPostCommentRequest {
    private ReportType reportType;
}
