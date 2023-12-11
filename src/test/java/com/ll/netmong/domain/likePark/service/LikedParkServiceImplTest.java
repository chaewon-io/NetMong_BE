package com.ll.netmong.domain.likePark.service;

import com.ll.netmong.domain.likePark.repository.LikedParkRepository;
import com.ll.netmong.domain.likedPost.exception.DuplicateLikeException;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LikedParkServiceImplTest {

    @Autowired
    private LikedParkServiceImpl likedParkService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikedParkRepository likedParkRepository;

    @Autowired
    private ParkRepository parkRepository;

    private Park park;
    private Member member;
    private UserDetails userDetails;
    private Long parkId;

    @BeforeEach
    void setUp() {
        parkId = 1L;
        park = Park.builder().id(parkId).likesCount(0L).likedParks(new ArrayList<>()).build();
        parkRepository.save(park);

        String username = "testUser" + UUID.randomUUID().toString();
        userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
        member = Member.builder().username(username).build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("좋아요 기능 동시성 테스트에서 실패한다.")
    public void testConcurrentLikes() throws InterruptedException {
        int threadCount = 100; // 동시 요청 수

        ExecutorService executorService = Executors.newFixedThreadPool(32); // 32개 스레드 생성
        CountDownLatch latch = new CountDownLatch(threadCount); // 스레드 완료 대기를 위해

        for(int i=0; i<threadCount; i++){
            executorService.submit(() -> {
                try {
                    likedParkService.addLikeToPark(park, userDetails);
                } catch (DuplicateLikeException e) {
                    // 예외 처리
                } finally {
                    latch.countDown(); //완료되었음을 알림
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        Park resultPark = parkRepository.findById(parkId).orElseThrow();
        System.out.println("Final likes count:" + resultPark.getLikesCount()); // 최종 좋아요 수 출력
        assertEquals(threadCount, resultPark.getLikesCount()); // 동시 요청 수만큼 좋아요가 증가했는지 검증
    }

    @Test
    @DisplayName("addLikeToPark() 메서드는 해당 공원, userDetails를 통해 좋아요를 추가하고 저장한다.")
    void testAddLikeToPark() {
        likedParkService.addLikeToPark(park, userDetails);
        assertTrue(likedParkRepository.existsByMemberAndPark(member, park));
    }

    @Test
    @DisplayName("addLikeToPark() 메서드는 이미 좋아요를 누른 공원에 대해 DuplicateLikeException을 발생시켜 중복 체크를 한다.")
    void testAddLikeToParkThrowsDuplicateLikeException() {
        likedParkService.addLikeToPark(park, userDetails);
        assertThrows(DuplicateLikeException.class, () -> likedParkService.addLikeToPark(park, userDetails));
    }

    @Test
    @DisplayName("getParkById() 메서드는 주어진 ID에 해당하는 Park를 반환한다.")
    void testGetParkById() {
        Park foundPark = likedParkService.getParkById(park.getId());
        assertEquals(park.getId(), foundPark.getId());
    }

    @Test
    @DisplayName("getParkById() 메서드는 주어진 ID에 해당하는 Park가 없을 경우 DataNotFoundException을 발생시킨다.")
    void testGetParkByIdThrowsDataNotFoundException() {
        Long nonExistingParkId = 99999L;
        assertThrows(DataNotFoundException.class, () -> likedParkService.getParkById(nonExistingParkId));
    }

    @Test
    @DisplayName("getMemberById() 메서드는 주어진 Username에 해당하는 Member 객체를 반환한다.")
    void testGetMemberById() {
        Member foundMember = likedParkService.getMemberById(userDetails);
        assertEquals(member.getUsername(), foundMember.getUsername());
    }

    @Test
    @DisplayName("getMemberById() 메서드는 주어진 Username에 해당하는 Member가 없을 경우 DataNotFoundException을 발생시킨다.")
    void testGetMemberByIdThrowsDataNotFoundException() {
        UserDetails nonExistingUserDetails = User.withUsername("nonExistingUser").password("testPassword").authorities("USER").build();
        assertThrows(DataNotFoundException.class, () -> likedParkService.getMemberById(nonExistingUserDetails));
    }

    @Test
    @DisplayName("countLikesToPark() 메서드는 해당 Park에 대한 좋아요 개수를 반환한다.")
    void testCountLikesToPark() {
        for (int i = 0; i < 10; i++) {
            String username = "liker" + UUID.randomUUID().toString();
            UserDetails userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
            Member liker = Member.builder().username(username).build();
            memberRepository.save(liker);
            likedParkService.addLikeToPark(park, userDetails);
        }

        Long actualCount = likedParkService.countLikesToPark(park);

        Long expectedCount = 10L;
        assertEquals(expectedCount, actualCount);
    }

    @Test
    @DisplayName("removeLikeFromPark() 메서드는 해당 공원, userDetails를 통해 좋아요를 제거한다.")
    void testRemoveLikeFromPark() {
        likedParkService.addLikeToPark(park, userDetails);
        assertTrue(likedParkRepository.existsByMemberAndPark(member, park));

        likedParkService.removeLikeFromPark(park, userDetails);
        assertFalse(likedParkRepository.existsByMemberAndPark(member, park));
    }

    @Test
    @DisplayName("removeLikeFromPark() 메서드는 해당 공원에 userDetails가 좋아요를 누르지 않았을 경우 IllegalArgumentException을 발생시킨다.")
    void testRemoveLikeFromParkThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> likedParkService.removeLikeFromPark(park, userDetails));
    }
}

