package com.ll.netmong.domain.reportPostComment.service;

import com.ll.netmong.common.ReportType;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.postComment.dto.response.ReportPostCommentResponse;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;
import com.ll.netmong.domain.reportPostComment.entity.ReportPostComment;
import com.ll.netmong.domain.reportPostComment.repository.ReportPostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportPostCommentServiceImpl implements ReportPostCommentService {

    private final ReportPostCommentRepository reportPostCommentRepository;
    private final PostCommentRepository postCommentRepository;

    @Override
    @Transactional
    public ReportPostCommentResponse reportComment(Long id, Member member, ReportType reportType) {
        PostComment comment = postCommentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 댓글이 없습니다. id: " + id));

        validateSelfReport(member, comment);
        validateDuplicateReport(member, comment);

        createAndSaveReport(comment, member, reportType);

        comment.increaseReportCount();
        comment.checkAndBlindComment();
        postCommentRepository.save(comment);

        return ReportPostCommentResponse.of(comment);
    }

    private void createAndSaveReport(PostComment comment, Member member, ReportType reportType) {
        ReportPostComment report = ReportPostComment.builder()
                .reportedPostComment(comment)
                .member(member)
                .reportType(reportType)
                .post(comment.getPost())
                .build();

        reportPostCommentRepository.save(report);
    }

    private void validateSelfReport(Member reporter, PostComment reportedComment) {
        if (reportedComment.getMemberID().equals(reporter)) {
            throw new IllegalArgumentException("자신의 댓글은 신고할 수 없습니다.");
        }
    }

    private void validateDuplicateReport(Member member, PostComment comment) {
        boolean alreadyReported = reportPostCommentRepository.existsByMemberAndReportedPostComment(member, comment);
        if (alreadyReported) {
            throw new IllegalArgumentException("이미 신고한 댓글입니다.");
        }
    }
}
