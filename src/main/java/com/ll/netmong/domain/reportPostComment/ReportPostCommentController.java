package com.ll.netmong.domain.reportPostComment;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.postComment.dto.request.ReportPostCommentRequest;
import com.ll.netmong.domain.postComment.dto.response.ReportPostCommentResponse;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import com.ll.netmong.domain.reportPostComment.service.ReportPostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/comment")
public class ReportPostCommentController {

    private final MemberRepository memberRepository;
    private final ReportPostCommentService reportPostCommentService;

    @PostMapping("/reports/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<ReportPostCommentResponse> reportComment(@PathVariable Long id,
                                                           @RequestBody ReportPostCommentRequest requestDto,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
        ReportPostCommentResponse responseDto = reportPostCommentService.reportComment(id, member, requestDto.getReportType());

        return RsData.successOf(responseDto);
    }
}
