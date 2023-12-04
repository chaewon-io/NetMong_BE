package com.ll.netmong.domain.reportPost.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import com.ll.netmong.domain.reportPost.dto.request.ReportPostRequest;
import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reportPost.entity.ReportPost;
import com.ll.netmong.domain.reportPost.exception.DuplicateReportException;
import com.ll.netmong.domain.reportPost.exception.InvalidReportException;
import com.ll.netmong.domain.reportPost.repository.ReportPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportPostServiceImpl implements ReportPostService {

    private final ReportPostRepository reportPostRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public ReportPostResponse reportPost(Long postId, ReportPostRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Member reporter = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
        Post reportedPost = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시물을 찾을 수 없습니다."));

        validateSelfReport(reporter, reportedPost);
        validateDuplicateReport(reporter, reportedPost);

        ReportPost reportPost = ReportPost.builder()
                .reporter(reporter)
                .reportedPost(reportedPost)
                .reportType(request.getReportType())
                .content(request.getContent())
                .build();

        reportPostRepository.save(reportPost);

        return new ReportPostResponse(reportPost.getId(), reporter.getId(), reportedPost.getId(), request.getReportType(), request.getContent());
    }

    private void validateSelfReport(Member reporter, Post reportedPost) {
        if (reportedPost.getMember().equals(reporter)) {
            throw new InvalidReportException("자신의 게시물은 신고할 수 없습니다.");
        }
    }

    private void validateDuplicateReport(Member reporter, Post reportedPost) {
        boolean alreadyReported = reportPostRepository.existsByReporterAndReportedPost(reporter, reportedPost);
        if (alreadyReported) {
            throw new DuplicateReportException("이미 신고한 게시물입니다.");
        }
    }
}
