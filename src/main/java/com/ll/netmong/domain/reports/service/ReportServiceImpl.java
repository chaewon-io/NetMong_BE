package com.ll.netmong.domain.reports.service;

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
import com.ll.netmong.domain.reports.repository.ReportCommentRepository;
import com.ll.netmong.domain.reports.repository.ReportPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ReportPostResponse reportPost(ReportRequest reportRequest, Long postId, UserDetails userDetails) {
        Post reportedPost = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 게시물을 찾을 수 없습니다."));

        Member reporter = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));

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
    public ReportCommentResponse reportComment(ReportRequest reportRequest, Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        PostComment reportedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 댓글을 찾을 수 없습니다."));

        Member reporter = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));

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

}
