package com.ll.netmong.domain.parkComment.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import com.ll.netmong.domain.parkComment.entity.ParkComment;
import com.ll.netmong.domain.parkComment.repository.ParkCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class ParkCommentServiceImplTest {

    @Autowired
    private ParkCommentServiceImpl parkCommentService;

    @MockBean
    private ParkRepository parkRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private ParkCommentRepository parkCommentRepository;

    private Park park;
    private Member member;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        Long parkId = 1L;
        park = Park.builder().id(parkId).comments(new ArrayList<>()).build();

        String username = "testUser";
        userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
        member = Member.builder().username(username).build();

        when(parkRepository.findById(parkId)).thenReturn(Optional.of(park));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
    }

    @Test
    @DisplayName("addParkComment() 메서드가 주어진 공원 ID, 댓글 내용, 사용자 정보를 이용하여 새로운 댓글을 생성하고, 저장하는지 검증한다.")
    void testAddParkCommentExists() {
        // Given
        ParkCommentRequest request = new ParkCommentRequest();
        request.setContent("TestContent");

        ParkComment comment = ParkComment.builder()
                .park(park)
                .memberID(member)
                .username(userDetails.getUsername())
                .content(request.getContent())
                .isDeleted(false)
                .build();
        when(parkCommentRepository.save(any(ParkComment.class))).thenReturn(comment);

        // When
        ParkCommentResponse response = parkCommentService.addParkComment(park.getId(), request, userDetails);

        // Then
        assertNotNull(response);
        assertEquals(comment.getContent(), response.getContent());
        assertEquals(comment.getUsername(), response.getUsername());
        assertFalse(response.getIsDeleted());
    }

    @Test
    @DisplayName("getCommentsOfPark() 메서드는 주어진 공원 ID에 해당하는 댓글 리스트를 반환한다.")
    void testGetCommentsOfPark() {
        // Given
        Long parkId = 1L;
        List<ParkComment> comments = Arrays.asList(
                ParkComment.builder().id(1L).park(park).memberID(member).username("user1").content("content1").isDeleted(false).build(),
                ParkComment.builder().id(2L).park(park).memberID(member).username("user2").content("content2").isDeleted(false).build()
        );
        when(parkCommentRepository.findByParkId(parkId)).thenReturn(comments);

        // When
        List<ParkCommentResponse> response = parkCommentService.getCommentsOfPark(parkId);

        // Then
        assertEquals(comments.size(), response.size());
        for (int i = 0; i < comments.size(); i++) {
            assertEquals(comments.get(i).getContent(), response.get(i).getContent());
            assertEquals(comments.get(i).getUsername(), response.get(i).getUsername());
            assertFalse(response.get(i).getIsDeleted());
        }
    }

    // 테스트에서는 UserDetails 객체와 댓글의 작성자가 동일하도록 설정하여 updateComment() 메서드 호출 시 AccessDeniedException이 발생하지 않도록 한다.
    @Test
    @DisplayName("updateComment() 메서드는 주어진 댓글 ID, 댓글 수정 요청, 사용자 정보를 이용하여 댓글을 수정한다.")
    void testUpdateComment() {
        // Given
        Long commentId = 1L;
        ParkComment originalComment = ParkComment.builder().id(commentId).park(park).memberID(member).username(userDetails.getUsername()).content("content1").isDeleted(false).build();
        ParkCommentRequest updateRequest = new ParkCommentRequest();
        updateRequest.setContent("updatedContent");

        when(parkCommentRepository.findById(commentId)).thenReturn(Optional.of(originalComment));
        when(parkCommentRepository.save(any(ParkComment.class))).thenReturn(originalComment);

        // When
        ParkCommentResponse response = parkCommentService.updateComment(commentId, updateRequest, userDetails);

        // Then
        assertEquals(updateRequest.getContent(), response.getContent());
        assertEquals(originalComment.getUsername(), response.getUsername());
        assertFalse(response.getIsDeleted());
    }

    @Test
    @DisplayName("deleteComment() 메서드는 주어진 댓글 ID, 사용자 정보를 이용하여 댓글을 삭제한다.")
    void testDeleteComment() {
        // Given
        Long commentId = 1L;
        ParkComment originalComment = ParkComment.builder().id(commentId).park(park).memberID(member).username(userDetails.getUsername()).content("content1").isDeleted(false).build();

        when(parkCommentRepository.findById(commentId)).thenReturn(Optional.of(originalComment));
        when(parkCommentRepository.save(any(ParkComment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        parkCommentService.deleteComment(commentId, userDetails);

        // Then
        assertTrue(originalComment.getIsDeleted());
    }

}


