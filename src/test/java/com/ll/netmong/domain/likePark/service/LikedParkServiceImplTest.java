package com.ll.netmong.domain.likePark.service;

import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.likePark.repository.LikedParkRepository;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikedParkServiceImplTest {

    @InjectMocks
    private LikedParkServiceImpl likedParkService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LikedParkRepository likedParkRepository;

    @Mock
    private ParkRepository parkRepository;

    private Park park;
    private Member member;
    private UserDetails userDetails;
    private Long parkId;

    @BeforeEach
    void setUp() {
        parkId = 1L;
        park = Park.builder().id(parkId).likesCount(0L).likedParks(new ArrayList<>()).build();
    }

    @Test
    @DisplayName("addLikeToPark() 메서드는 해당 공원, userDetails를 통해 좋아요를 추가하고 저장한다.")
    void testAddLikeToPark() {
        String username = "testUser";
        userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
        member = Member.builder().username(username).build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(likedParkRepository.existsByMemberAndPark(member, park)).thenReturn(false);

        likedParkService.addLikeToPark(park, userDetails);

        verify(likedParkRepository, times(1)).save(any(LikedPark.class));
    }

    @Test
    @DisplayName("getParkById() 메서드는 주어진 ID에 해당하는 Park를 반환한다.")
    void testGetParkById() {
        when(parkRepository.findById(parkId)).thenReturn(Optional.of(park));

        Park foundPark = likedParkService.getParkById(parkId);

        assertEquals(park, foundPark);
    }

    @Test
    @DisplayName("getMemberById() 메서드는 주어진 ID에 해당하는 Member 객체를 반환한다.")
    void testGetMemberById() {
        String username = "testUser";
        UserDetails userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
        Member member = Member.builder().username(username).build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));

        Member foundMember = likedParkService.getMemberById(userDetails);

        assertEquals(member, foundMember);
    }

    @Test
    @DisplayName("getMemberById() 메서드는 주어진 ID에 해당하는 Member가 없을 경우 DataNotFoundException을 발생시킨다.")
    void testGetMemberByIdThrowsDataNotFoundException() {
        String username = "testUser";
        UserDetails userDetails = User.withUsername(username).password("testPassword").authorities("USER").build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> likedParkService.getMemberById(userDetails));
    }

    @Test
    @DisplayName("countLikesToPark() 메서드는 해당 Park에 대한 좋아요 개수를 반환한다.")
    void testCountLikesToPark() {
        Long parkId = 1L;
        Park park = Park.builder().id(parkId).likesCount(0L).likedParks(new ArrayList<>()).build();
        Long expectedCount = 10L;
        when(likedParkRepository.countLikesByPark(park)).thenReturn(expectedCount);

        Long actualCount = likedParkService.countLikesToPark(park);

        assertEquals(expectedCount, actualCount);
        System.out.println("Expected: " + expectedCount + ", Actual: " + actualCount);
    }

}
