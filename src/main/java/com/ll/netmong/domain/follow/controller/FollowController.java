package com.ll.netmong.domain.follow.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.follow.service.FollowService;
import com.ll.netmong.domain.member.dto.UsernameRequest;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;

    @PostMapping("/follow")
    public RsData follow(@RequestBody UsernameRequest usernameRequest, @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        //팔로우 하는 사람
        String followerEmail = userDetails.getUsername();
        Member follower = memberService.findByEmail(followerEmail);

        //팔로우 받는 사람
        Member followee = memberService.findByUsername(usernameRequest.getUsername());

        if (follower.getUsername().equals(usernameRequest.getUsername())) {
            return RsData.failOf("follow 실패");
        }

        followService.follow(follower, followee);

        return RsData.successOf("follow 성공");
    }

    @PostMapping("/unfollow")
    public RsData unfollow(@RequestBody UsernameRequest usernameRequest, @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        //언팔로우 하는 사람
        String followerEmail = userDetails.getUsername();
        Member follower = memberService.findByEmail(followerEmail);

        //언팔로우 받는 사람
        Member followee = memberService.findByUsername(usernameRequest.getUsername());
        followService.unfollow(follower, followee);

        return RsData.successOf("unfollow 성공");
    }
}
