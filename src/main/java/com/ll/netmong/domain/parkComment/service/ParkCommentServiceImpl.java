package com.ll.netmong.domain.parkComment.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import com.ll.netmong.domain.parkComment.entity.ParkComment;
import com.ll.netmong.domain.parkComment.repository.ParkCommentRepository;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParkCommentServiceImpl implements ParkCommentService {

    private final ParkCommentRepository parkCommentRepository;
    private final ParkRepository parkRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ParkCommentResponse addParkComment(Long parkId, ParkCommentRequest parkCommentRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 공원을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));

        ParkComment comment = ParkComment.builder()
                .park(park)
                .memberID(member)
                .username(userDetails.getUsername())
                .content(parkCommentRequest.getContent())
                .isDeleted(false)
                .build();
        park.addComment(comment);
        ParkComment savedComment = parkCommentRepository.save(comment);
        return savedComment.toResponse();
    }

    @Override
    @Transactional
    public List<ParkCommentResponse> getCommentsOfPark(Long parkId) {
        List<ParkComment> comments = parkCommentRepository.findByParkId(parkId);
        return comments.stream().map(ParkComment::toResponse).collect(Collectors.toList());
    }

}
