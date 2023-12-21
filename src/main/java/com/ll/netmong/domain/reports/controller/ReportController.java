package com.ll.netmong.domain.reports.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.service.PostService;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import com.ll.netmong.domain.reports.dto.request.ReportRequest;
import com.ll.netmong.domain.reports.dto.response.ReportCommentResponse;
import com.ll.netmong.domain.reports.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reports.service.ReportService;
import com.ll.netmong.domain.reports.util.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;
    private final PostService postService;
    private final MemberService memberService;
    private final PostCommentService postCommentService;

    @PostMapping("/post/{postId}")
    public RsData<ReportPostResponse> reportPost(@RequestBody ReportRequest reportRequest, @PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Post reportedPost = postService.findByPostId(postId);
        Member reporter = memberService.findByEmail(userDetails.getUsername());
        ReportPostResponse reportPostResponse = reportService.reportPost(reportRequest, reportedPost, reporter);
        return RsData.successOf(reportPostResponse);
    }

    @PostMapping("/comment/{commentId}")
    public RsData<ReportCommentResponse> reportComment(@RequestBody ReportRequest reportRequest, @PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        PostComment reportedComment = postCommentService.findByCommentId(commentId);
        Member reporter = memberService.findByEmail(userDetails.getUsername());
        ReportCommentResponse reportCommentResponse = reportService.reportComment(reportRequest, reportedComment, reporter);

        return RsData.successOf(reportCommentResponse);
    }

    @GetMapping("/types")
    public RsData<List<String>> getReportTypes() {
        List<String> reportTypes = Arrays.stream(ReportType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return RsData.successOf(reportTypes);
    }
}
