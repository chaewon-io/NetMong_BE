package com.ll.netmong.domain.admin.dto.reponse;

import com.ll.netmong.common.ReportType;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.reportPostComment.entity.ReportPostComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportPostCommentDetailsResponse {

    private Long id;
    private Integer reportCount;
    private Boolean isBlinded;
    private ReportType reportType;
    private String content;
    private Long postId;

    public static ReportPostCommentDetailsResponse of(ReportPostComment reportPostComment) {
        PostComment reportedPostComment = reportPostComment.getReportedPostComment();

        return new ReportPostCommentDetailsResponse(
                reportedPostComment.getId(),
                reportedPostComment.getReportCount(),
                reportedPostComment.getIsBlinded(),
                reportPostComment.getReportType(),
                reportedPostComment.getContent(),
                reportedPostComment.getPost().getId()
        );
    }
}
