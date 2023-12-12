package com.ll.netmong.domain.reports.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.reports.util.ReportType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportComment extends BaseEntity implements Report<PostComment> {

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "reported_comment_id", nullable = false)
    private PostComment reportedComment;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(nullable = false)
    private String content;

    @Override
    public Member getReporter() {
        return this.reporter;
    }

    @Override
    public PostComment getReported() {
        return this.reportedComment;
    }

    @Override
    public ReportType getReportType() {
        return this.reportType;
    }
}
