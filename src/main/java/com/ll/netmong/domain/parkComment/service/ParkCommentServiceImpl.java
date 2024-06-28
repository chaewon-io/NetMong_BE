package com.ll.netmong.domain.parkComment.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.exception.MemberNotFoundException;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.exception.ParkNotFoundException;
import com.ll.netmong.domain.park.repository.ParkRepository;
import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import com.ll.netmong.domain.parkComment.entity.ParkComment;
import com.ll.netmong.domain.parkComment.exception.ParkCommentNotFoundException;
import com.ll.netmong.domain.parkComment.repository.ParkCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParkCommentServiceImpl implements ParkCommentService {

    private final ParkCommentRepository parkCommentRepository;
    private final ParkRepository parkRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ParkCommentResponse addParkComment(Long parkId, ParkCommentRequest parkCommentRequest, UserDetails userDetails) {
        try {
            Park park = getParkById(parkId);
            Member member = getMemberById(userDetails);

            park.updatePetAllowed(parkCommentRequest.getPetAllowed());

            ParkComment comment = ParkComment.toEntity(park, member, parkCommentRequest.getContent());
            park.addComment(comment);
            ParkComment savedComment = parkCommentRepository.save(comment);

            return ParkCommentResponse.from(savedComment);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("다른 사용자가 동시에 데이터를 수정하였습니다. 다시 시도해주세요.");
        }
    }

    @Override
    @Transactional
    public Page<ParkCommentResponse> getCommentsOfPark(Long parkId, Pageable pageable) {
        Page<ParkComment> comments = parkCommentRepository.findByParkIdAndIsDeletedFalse(parkId, pageable);
        return comments.map(ParkCommentResponse::from);
    }

    @Override
    @Transactional
    public ParkCommentResponse updateComment(Long commentId, ParkCommentRequest updateRequest, UserDetails userDetails) {

        ParkComment comment = getByParkCommentId(commentId);

        checkCommentAuthor(comment, userDetails);
        comment.updateContent(updateRequest.getContent());
        ParkComment updatedComment = parkCommentRepository.save(comment);
        return ParkCommentResponse.from(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, UserDetails userDetails) {
        ParkComment comment = getByParkCommentId(commentId);

        checkCommentAuthor(comment, userDetails);
        comment.markAsDeleted(true);
        parkCommentRepository.save(comment);
    }

    private void checkCommentAuthor(ParkComment comment, UserDetails userDetails) {
        Member member = getMemberById(userDetails);

        if (!comment.getUsername().equals(member.getUsername())) {
            throw new AccessDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
    }

    private Member getMemberById(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Park getParkById(Long parkId) {
        return parkRepository.findWithOptimisticLockById(parkId)
                .orElseThrow(() -> new ParkNotFoundException("해당하는 공원을 찾을 수 없습니다."));
    }

    private ParkComment getByParkCommentId(Long commentId) {
        return parkCommentRepository.findById(commentId)
                .orElseThrow(() -> new ParkCommentNotFoundException("해당하는 댓글을 찾을 수 없습니다."));
    }

}