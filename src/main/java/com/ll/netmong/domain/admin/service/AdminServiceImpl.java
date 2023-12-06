package com.ll.netmong.domain.admin.service;

import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reportPost.entity.ReportPost;
import com.ll.netmong.domain.reportPost.repository.ReportPostRepository;
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

    @Override
    public List<ReportPostResponse> getAllReportPosts() {
        List<ReportPost> reports = reportPostRepository.findAll();
        return reports.stream()
                .map(ReportPost::toResponse)
                .collect(Collectors.toList());
    }
}
