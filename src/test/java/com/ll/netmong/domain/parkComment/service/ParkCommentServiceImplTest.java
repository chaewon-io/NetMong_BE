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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private Long parkId;

    @BeforeEach
    void setUp() {
        parkId = 1L;
        park = Park.builder().id(parkId).comments(new ArrayList<>()).build();
        parkRepository.save(park);

        String username = "testUser" + UUID.randomUUID().toString();
        userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
        member = Member.builder().username(username).build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("addParkComment() 메서드는 댓글을 추가하고 저장한다.")
    void AddParkComment() {
        ParkCommentRequest parkCommentRequest = new ParkCommentRequest();
        parkCommentRequest.setContent("Test Comment");

        ParkCommentResponse result = parkCommentService.addParkComment(parkId, parkCommentRequest, userDetails);

        ParkComment savedComment = parkCommentRepository.findById(result.getId())
                .orElseThrow(() -> new DataNotFoundException("댓글을 찾을 수 없습니다."));

        assertEquals(savedComment.getContent(), parkCommentRequest.getContent());
        assertEquals(savedComment.getUsername(), userDetails.getUsername());
    }

    @Test
    @DisplayName("addParkComment() 메서드는 parkId에 해당하는 공원이 없을 경우 DataNotFoundException을 발생시킨다.")
    void AddParkComment_WhenFindById_ThrowDataNotFound() {
        Long invalidParkId = -1L;
        ParkCommentRequest parkCommentRequest = new ParkCommentRequest();
        parkCommentRequest.setContent("Test Comment");

        assertThrows(DataNotFoundException.class, () -> {
            parkCommentService.addParkComment(invalidParkId, parkCommentRequest, userDetails);
        });
    }

    @Test
    @DisplayName("addParkComment() 메서드는 userDetails의 유저가 없을 경우 DataNotFoundException을 발생시킨다.")
    void AddParkComment_When_FindByUsername_ThrowDataNotFound() {
        ParkCommentRequest parkCommentRequest = new ParkCommentRequest();
        parkCommentRequest.setContent("Test Comment");
        UserDetails invalidUserDetails = User.withUsername("invalidUser").password("testPassword").authorities("USER").build();

        assertThrows(DataNotFoundException.class, () -> {
            parkCommentService.addParkComment(parkId, parkCommentRequest, invalidUserDetails);
        });
    }
}

