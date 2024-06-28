package com.ll.netmong.domain.likePark.service;

import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.likePark.exception.DuplicateLikedParkException;
import com.ll.netmong.domain.likePark.repository.LikedParkRepository;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
        likedParkRepository.deleteAll();
        parkRepository.deleteAll();

        park = Park.builder()
                .parkNm("Test Park")
                .lnmadr("Test Address")
                .latitude(37.5665)
                .longitude(126.9780)
                .phoneNumber("010-1234-5678")
                .state("Test State")
                .city("Test City")
                .likesCount(0L)
                .likedParks(new ArrayList<>())
                .build();

        parkRepository.save(park);
        parkId = park.getId();

        member = Member.builder()
                .email("test@example.com")
                .build();
        memberRepository.save(member);

        userDetails = User.withUsername(member.getEmail())
                .password("testPassword")
                .authorities("USER")
                .build();
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 같은 공원에 '좋아요'를 누르는 상황에서, 이로 인해 OptimisticLockingFailureException가 발생한다.")
    public void testConcurrentLikes3() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        // '좋아요'를 누르는 작업을 정의
        Runnable likeTask = () -> {
            String username = "testUser" + UUID.randomUUID().toString();
            UserDetails userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();

            assertThatThrownBy(() -> likedParkService.addLikeToPark(parkId, userDetails))
                    .isInstanceOf(OptimisticLockingFailureException.class);
        };

        // '좋아요' 1000번
        for (int i = 0; i < 1000; i++) {
            executorService.submit(likeTask);
        }

        // 모든 작업이 완료될 때까지 대기
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                // 60초 동안 작업이 완료되지 않으면 에러를 출력하고 종료
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 같은 공원에 '좋아요'를 누르고 취소하는 상황에서, 이로 인해 OptimisticLockingFailureException가 발생한다.")
    public void testConcurrentLikesAndDislikes() {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        // '좋아요'를 누르는 작업을 정의
        Runnable likeTask = () -> {
            String username = "testUser" + UUID.randomUUID().toString();
            UserDetails userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();

            assertThatThrownBy(() -> likedParkService.addLikeToPark(parkId, userDetails))
                    .isInstanceOf(OptimisticLockingFailureException.class);
        };

        // '좋아요'를 취소하는 작업을 정의
        Runnable dislikeTask = () -> {
            String username = "testUser" + UUID.randomUUID().toString();
            UserDetails userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();

            assertThatThrownBy(() -> likedParkService.removeLikeFromPark(parkId, userDetails))
                    .isInstanceOf(OptimisticLockingFailureException.class);
        };

        // '좋아요' 작업을 60번 실행함
        for (int i = 0; i < 60; i++) {
            executorService.submit(likeTask);
        }

        // '좋아요 취소' 작업을 40번 실행함
        for (int i = 0; i < 40; i++) {
            executorService.submit(dislikeTask);
        }

        // 모든 작업이 완료될 때까지 대기
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }

    @Test
    @DisplayName("addLikeToPark() 메서드는 해당 공원, userDetails를 통해 좋아요를 추가하고 저장한다.")
    void testAddLikeToPark() {
        likedParkService.addLikeToPark(parkId, userDetails);
        assertTrue(likedParkRepository.existsByMemberAndPark(member, park));
    }

    @Test
    @DisplayName("addLikeToPark() 메서드는 이미 좋아요를 누른 공원에 대해 DuplicateLikeException을 발생시켜 중복 체크를 한다.")
    void testAddLikeToParkThrowsDuplicateLikeException() {
        likedParkService.addLikeToPark(parkId, userDetails);
        assertThrows(DuplicateLikedParkException.class, () -> likedParkService.addLikeToPark(parkId, userDetails));
    }

    @Test
    @DisplayName("countLikesToPark() 메서드는 해당 Park에 대한 좋아요 개수를 반환한다.")
    void testCountLikesToPark() {
        for (int i = 0; i < 10; i++) {
            String username = "liker" + UUID.randomUUID().toString();
            UserDetails userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
            Member liker = Member.builder().email(username).build();
            memberRepository.save(liker);
            likedParkService.addLikeToPark(parkId, userDetails);
        }

        Long actualCount = likedParkService.countLikesToPark(parkId);

        Long expectedCount = 10L;
        assertEquals(expectedCount, actualCount);
    }

    @Test
    @DisplayName("removeLikeFromPark() 메서드는 해당 공원, userDetails를 통해 좋아요를 제거한다.")
    void testRemoveLikeFromPark() {
        likedParkService.addLikeToPark(parkId, userDetails);
        assertTrue(likedParkRepository.existsByMemberAndPark(member, park));

        likedParkService.removeLikeFromPark(parkId, userDetails);
        assertFalse(likedParkRepository.existsByMemberAndPark(member, park));
    }

    @Test
    @DisplayName("getLikedParksByMember() 메서드는 특정 사용자가 좋아요를 누른 공원 목록을 반환해야 한다.")
    void testGetLikedParksByMember() {
        LikedPark likedPark = LikedPark.builder()
                .member(member)
                .park(park)
                .build();

        likedParkRepository.save(likedPark);

        List<ParkResponse> result = likedParkService.getLikedParksForMember(userDetails);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(park.getId());
        assertThat(result.get(0).getParkNm()).isEqualTo(park.getParkNm());
    }

}

