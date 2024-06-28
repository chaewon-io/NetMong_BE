package com.ll.netmong.domain.parkComment.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.exception.ParkNotFoundException;
import com.ll.netmong.domain.park.repository.ParkRepository;
import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import com.ll.netmong.domain.parkComment.entity.ParkComment;
import com.ll.netmong.domain.parkComment.exception.ParkCommentNotFoundException;
import com.ll.netmong.domain.parkComment.repository.ParkCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ParkCommentServiceImplTest {

    @Autowired
    private ParkCommentServiceImpl parkCommentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ParkCommentRepository parkCommentRepository;

    @Autowired
    private ParkRepository parkRepository;

    private Park park;
    private Member member;
    private UserDetails userDetails;
    private ParkCommentRequest parkCommentRequest;

    @BeforeEach
    void setUp() {
        park = Park.builder().comments(new ArrayList<>()).build();
        park = parkRepository.save(park);

        String testUsername = "testUser" + UUID.randomUUID().toString();
        userDetails = User.withUsername(testUsername).password("testPassword").authorities("USER").build();
        member = Member.builder().email(userDetails.getUsername()).username(testUsername).build();
        memberRepository.save(member);

        parkCommentRequest = new ParkCommentRequest();
        parkCommentRequest.setContent("Test Comment");
    }

    @Test
    @DisplayName("addParkComment() 메서드는 댓글을 추가하고 저장하며 반려동물 출입 여부를 업데이트한다.")
    void testAddParkComment() {

        parkCommentRequest.setPetAllowed(true);

        ParkCommentResponse result = parkCommentService.addParkComment(park.getId(), parkCommentRequest, userDetails);

        ParkComment savedComment = parkCommentRepository.findById(result.getId())
                .orElseThrow(() -> new ParkCommentNotFoundException("댓글을 찾을 수 없습니다."));

        assertEquals(savedComment.getContent(), parkCommentRequest.getContent(), "댓글의 내용이 일치하지 않습니다.");
        assertEquals(savedComment.getUsername(), member.getUsername(), "댓글 작성자의 이름이 일치하지 않습니다.");

        // park의 petAllowed가 정확하게 업데이트 되었는지
        Park updatedPark = parkRepository.findById(park.getId())
                .orElseThrow(() -> new ParkNotFoundException("공원을 찾을 수 없습니다."));

        assertEquals(updatedPark.getPetAllowed(), parkCommentRequest.getPetAllowed(), "공원의 반려동물 출입 여부가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("getCommentsOfPark() 메서드는 해당 공원의 댓글 페이징 처리를 검증한다.")
    void testPagination() {

        parkCommentRequest.setPetAllowed(true);

        // 5개 댓글 추가
        for (int i = 0; i < 5; i++) {
            parkCommentService.addParkComment(park.getId(), parkCommentRequest, userDetails);
        }

        // 페이지에 댓글 3개 요청
        Pageable pageable = PageRequest.of(0, 3);

        Page<ParkCommentResponse> comments = parkCommentService.getCommentsOfPark(park.getId(), pageable);

        assertEquals(5, comments.getTotalElements());
        assertEquals(3, comments.getContent().size());
    }

    @Test
    @DisplayName("getCommentsOfPark() 메서드는 논리 삭제된 댓글의 반환값을 보고 제공한다.")
    void testGetCommentsOfPark() {

        parkCommentRequest.setPetAllowed(true);

        // 5개 댓글 추가 후 2개 논리 삭제
        for (int i = 0; i < 5; i++) {
            ParkCommentResponse comment = parkCommentService.addParkComment(park.getId(), parkCommentRequest, userDetails);
            if (i < 2) {
                parkCommentService.deleteComment(comment.getId(), userDetails);
            }
        }

        // 페이지에 댓글 10개 요청
        Pageable pageable = PageRequest.of(0, 10);

        Page<ParkCommentResponse> comments = parkCommentService.getCommentsOfPark(park.getId(), pageable);

        // 논리적으로 삭제되지 않은 댓글 : 3개, 첫 페이지 댓글 수 : 3개
        assertEquals(3, comments.getTotalElements());
        assertEquals(3, comments.getContent().size());
    }

    @Test
    @DisplayName("updateComment() 메서드는 댓글의 내용을 수정한다.")
    void testUpdateComment() {

        parkCommentRequest.setPetAllowed(true);

        ParkCommentResponse comment = parkCommentService.addParkComment(park.getId(), parkCommentRequest, userDetails);

        ParkCommentRequest updateRequest = new ParkCommentRequest();
        updateRequest.setContent("Updated Comment");

        ParkCommentResponse updatedComment = parkCommentService.updateComment(comment.getId(), updateRequest, userDetails);

        assertEquals("Updated Comment", updatedComment.getContent());
    }

    @Test
    @DisplayName("updateComment() 메서드는 존재하지 않는 댓글을 수정하려 할 경우 ParkCommentNotFoundException을 발생시킨다.")
    void testUpdateComment_exception() {
        Long nonExistentCommentId = 999999L;

        ParkCommentRequest updateRequest = new ParkCommentRequest();
        updateRequest.setContent("Updated Comment");

        assertThrows(ParkCommentNotFoundException.class, () -> parkCommentService.updateComment(nonExistentCommentId, updateRequest, userDetails));
    }

    @Test
    @DisplayName("deleteComment() 메서드는 댓글을 논리 삭제하며, 존재하지 않는 댓글에 대해서는 ParkCommentNotFoundException을 발생시킨다.")
    void testDeleteComment() {

        parkCommentRequest.setPetAllowed(true);

        ParkCommentResponse comment = parkCommentService.addParkComment(park.getId(), parkCommentRequest, userDetails);

        parkCommentService.deleteComment(comment.getId(), userDetails);

        ParkComment deletedComment = parkCommentRepository.findById(comment.getId())
                .orElseThrow(() -> new ParkCommentNotFoundException("해당 댓글이 없습니다. id: " + comment.getId()));
        assertTrue(deletedComment.getIsDeleted());
    }
}