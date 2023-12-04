package com.ll.netmong.domain.reportPost.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import com.ll.netmong.domain.reportPost.dto.request.ReportPostRequest;
import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reportPost.entity.ReportPost;
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

        ReportPost reportPost = ReportPost.builder()
                .reporter(reporter)
                .reportedPost(reportedPost)
                .reportType(request.getReportType())
                .content(request.getReportContent())
                .build();

        reportPostRepository.save(reportPost);

        return new ReportPostResponse(reportPost.getId(), reporter.getId(), reportedPost.getId(), request.getReportType());
    }
}
