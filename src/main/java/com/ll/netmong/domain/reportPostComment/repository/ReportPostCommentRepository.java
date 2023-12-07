package com.ll.netmong.domain.reportPostComment.repository;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.reportPostComment.entity.ReportPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPostCommentRepository extends JpaRepository<ReportPostComment, Long> {
    boolean existsByMemberAndReportedPostComment(Member member, PostComment comment);
}
