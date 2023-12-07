package com.ll.netmong.domain.postComment.dto.response;

import com.ll.netmong.domain.postComment.entity.PostComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportPostCommentResponse {
    private Long id;
    private Integer reportCount;
    private Boolean isBlinded;

    public static ReportPostCommentResponse of(PostComment comment) {
        return new ReportPostCommentResponse(
                comment.getId(),
                comment.getReportCount(),
                comment.getIsBlinded()
        );
    }
}
