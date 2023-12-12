package com.ll.netmong.domain.reports.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
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
public class ReportPost extends BaseEntity implements Report<Post> {

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "reported_post_id", nullable = false)
    private Post reportedPost;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(nullable = false)
    private String content;

    @Override
    public Member getReporter() {
        return this.reporter;
    }

    @Override
    public Post getReported() {
        return this.reportedPost;
    }

    @Override
    public ReportType getReportType() {
        return this.reportType;
    }


}
