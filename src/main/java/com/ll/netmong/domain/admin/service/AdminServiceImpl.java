package com.ll.netmong.domain.admin.service;

import com.ll.netmong.domain.admin.dto.reponse.ReportPostCommentDetailsResponse;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;
import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reportPost.entity.ReportPost;
import com.ll.netmong.domain.reportPost.repository.ReportPostRepository;
import com.ll.netmong.domain.reportPostComment.entity.ReportPostComment;
import com.ll.netmong.domain.reportPostComment.repository.ReportPostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final ReportPostRepository reportPostRepository;
    private final PostCommentRepository postCommentRepository;
    private final ReportPostCommentRepository reportPostCommentRepository;

    @Override
    public List<ReportPostResponse> getAllReportPosts() {
        List<ReportPost> reports = reportPostRepository.findAll();
        return reports.stream()
                .map(ReportPost::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportPostCommentDetailsResponse> getAllReportedComments() {
        List<ReportPostComment> reportedComments = reportPostCommentRepository.findAll();
        return reportedComments.stream()
                .map(ReportPostCommentDetailsResponse::of)
                .collect(Collectors.toList());
    }
}
