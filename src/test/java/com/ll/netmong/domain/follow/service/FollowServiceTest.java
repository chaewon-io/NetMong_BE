package com.ll.netmong.domain.follow.service;

import com.ll.netmong.domain.follow.dto.FollowCountDto;
import com.ll.netmong.domain.follow.entity.Follow;
import com.ll.netmong.domain.follow.exception.NotFollowedException;
import com.ll.netmong.domain.member.dto.JoinRequest;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class FollowServiceTest {

    Member followee;
    Member follower;
    Member member3;
    Member member4;
    Member member5;
    @Autowired
    private FollowService followService;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        //given
        JoinRequest joinRequest1 = new JoinRequest();
        joinRequest1.setUsername("username1");
        joinRequest1.setEmail("email@mam1.com");
        joinRequest1.setPassword("password1");
        joinRequest1.setRealname("real11");

        JoinRequest joinRequest2 = new JoinRequest();
        joinRequest2.setUsername("username2");
        joinRequest2.setEmail("email@mam2.com");
        joinRequest2.setPassword("password1");
        joinRequest2.setRealname("real22");

        JoinRequest joinRequest3 = new JoinRequest();
        joinRequest3.setUsername("username3");
        joinRequest3.setEmail("email@mam3.com");
        joinRequest3.setPassword("password1");
        joinRequest3.setRealname("real33");

        JoinRequest joinRequest4 = new JoinRequest();
        joinRequest4.setUsername("username4");
        joinRequest4.setEmail("email@mam4.com");
        joinRequest4.setPassword("password1");
        joinRequest4.setRealname("real44");

        JoinRequest joinRequest5 = new JoinRequest();
        joinRequest5.setUsername("username5");
        joinRequest5.setEmail("email@mam5.com");
        joinRequest5.setPassword("password1");
        joinRequest5.setRealname("real55");

        followee = memberService.createMember(joinRequest1);
        follower = memberService.createMember(joinRequest2);
        member3 = memberService.createMember(joinRequest3);
        member4 = memberService.createMember(joinRequest4);
        member5 = memberService.createMember(joinRequest5);
    }

    @Test
    @DisplayName("follow()는 follower와 followee를 받아서 follow를 생성 후 저장한다. id를 리턴한다.")
    public void followTest() {

        //when
        Long followId = followService.follow(follower, followee);

        //then
        Follow follow = followService.findById(followId);

        assertThat(follow.getFollower()).isEqualTo(follower);
        assertThat(follow.getFollowee()).isEqualTo(followee);
    }

    @Test
    @DisplayName("unfollow()는 follow를 찾고, 삭제한다.")
    public void unfollowTest() throws Exception {
        //given
        Long followId = followService.follow(follower, followee);

        //when
        followService.unfollow(follower, followee);

        //then
        assertThatThrownBy(() -> followService.findById(followId))
                .isInstanceOf(NotFollowedException.class);
    }

    @Test
    @DisplayName("unfollow()는 follow중이 아니면 NotFollowedException을 던진다.")
    public void unfollowTestException() {
        //when
        //then
        assertThatThrownBy(() -> followService.unfollow(follower, followee))
                .isInstanceOf(NotFollowedException.class);
    }

    @Test
    @DisplayName("countFollowerAndFollowee는 멤버를 받아 팔로워 수와 팔로잉 수를 dto로 반환한다.")
    public void countFollowerAndFolloweeTest() throws Exception {
        //given
        followService.follow(member3, followee);
        followService.follow(member3, member4);
        followService.follow(member3, member5);

        followService.follow(member4, member3);
        followService.follow(follower, member3);

        //when
        FollowCountDto followCountDto = followService.countFollowerAndFollowee(member3);

        //then 팔로워2, 팔로잉3
        assertThat(followCountDto.getFolloweeCount()).isEqualTo(2L);
        assertThat(followCountDto.getFollowerCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("isFollowing()은 follower와 followee를 받아 이미 팔로우 중이면 true를 반환한다.")
    public void isFollowingTest() throws Exception {

        //given
        followService.follow(member3, member4);

        //when
        Boolean following = followService.isFollowing(member3, member4);

        //then
        assertThat(following).isTrue();
    }
}