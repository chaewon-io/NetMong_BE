package com.ll.netmong.domain.reports.service;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;
import com.ll.netmong.domain.reports.dto.request.ReportRequest;
import com.ll.netmong.domain.reports.dto.response.ReportCommentResponse;
import com.ll.netmong.domain.reports.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reports.entity.ReportComment;
import com.ll.netmong.domain.reports.entity.ReportPost;
import com.ll.netmong.domain.reports.exception.DuplicateReportException;
import com.ll.netmong.domain.reports.exception.InvalidReportException;
import com.ll.netmong.domain.reports.repository.ReportCommentRepository;
import com.ll.netmong.domain.reports.repository.ReportPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportPostRepository reportPostRepository;
    private final ReportCommentRepository reportCommentRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ReportPostResponse reportPost(ReportRequest reportRequest, Post reportedPost, Member reporter) {
        if (reportedPost.getMember().equals(reporter)) {
            throw new InvalidReportException("자신의 게시물에 대한 신고는 허용되지 않습니다.");
        }

        if (reportPostRepository.existsByReporterAndReportedPost(reporter, reportedPost)) {
            throw new DuplicateReportException("이미 신고한 게시물에 대한 중복 신고는 허용되지 않습니다.");
        }

        ReportPost reportPost = ReportPost.builder()
                .reporter(reporter)
                .reportedPost(reportedPost)
                .reportType(reportRequest.getReportType())
                .content(reportRequest.getContent())
                .build();
        reportPostRepository.save(reportPost);

        return new ReportPostResponse(reportPost);
    }

    @Override
    @Transactional
    public ReportCommentResponse reportComment(ReportRequest reportRequest, PostComment reportedComment, Member reporter) {
        if (reportedComment.getMemberID().equals(reporter)) {
            throw new InvalidReportException("자신의 게시물에 대한 신고는 허용되지 않습니다.");
        }

        if (reportCommentRepository.existsByReporterAndReportedComment(reporter, reportedComment)) {
            throw new DuplicateReportException("이미 신고한 게시물에 대한 중복 신고는 허용되지 않습니다.");
        }

        ReportComment reportComment = ReportComment.builder()
                .reporter(reporter)
                .reportedComment(reportedComment)
                .reportType(reportRequest.getReportType())
                .content(reportRequest.getContent())
                .build();
        reportCommentRepository.save(reportComment);

        // 신고 횟수 증가 및 블라인드 처리 여부 판단
        reportedComment.increaseReportCount();
        reportedComment.checkAndBlindComment();

        return new ReportCommentResponse(reportComment);
    }

    @Override
    public List<ReportPostResponse> getReportedPosts() {
        List<ReportPost> reportedPosts = reportPostRepository.findAll();

        return reportedPosts.stream()
                .map(ReportPostResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportCommentResponse> getReportedComments() {
        List<ReportComment> reportedComments = reportCommentRepository.findAll();

        return reportedComments.stream()
                .map(ReportCommentResponse::new)
                .collect(Collectors.toList());
    }

}
