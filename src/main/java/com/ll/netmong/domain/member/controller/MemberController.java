package com.ll.netmong.domain.member.controller;

import com.ll.netmong.base.jwt.TokenDto;
import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.cart.service.CartService;
import com.ll.netmong.domain.follow.dto.FollowCountDto;
import com.ll.netmong.domain.follow.service.FollowService;
import com.ll.netmong.domain.member.dto.*;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CartService cartService;
    private final FollowService followService;

    @GetMapping("/find")
    public Member findMember() {
        return memberService.findById(1L);
    }

    @PostMapping("/join")
    public RsData<String> join(@Valid @RequestBody JoinRequest joinRequest) throws Exception {

        String username = memberService.createMember(joinRequest).getUsername();
        cartService.createCart(memberService.findByEmail(joinRequest.getEmail()));

        return RsData.successOf(username);
    }

    @PostMapping("/dup-username")
    public RsData checkDupUsername(@Valid @RequestBody UsernameRequest usernameRequest) {

        if (memberService.isDuplicateUsername(usernameRequest)) {
            return RsData.of("F-1", "이미 중복된 아이디가 있습니다.");
        }

        return RsData.of("S-1", "사용가능한 아이디입니다.");
    }

    @PostMapping("/dup-email")
    public RsData checkDupUsername(@Valid @RequestBody EmailRequest emailRequest) {

        if (memberService.isDuplicateEmail(emailRequest)) {
            return RsData.of("F-1", "이미 중복된 이메일이 있습니다.");
        }

        return RsData.of("S-1", "사용가능한 이메일입니다.");
    }

    @PostMapping("/login")
    public RsData<TokenDto> login(@Valid @RequestBody LoginDto loginDto) throws Exception {

        TokenDto tokenDto = memberService.login(loginDto);

        return RsData.successOf(tokenDto);
    }

    @PostMapping("/logout")
    public RsData logout() {
        return RsData.successOf("logout success");
    }

    @PatchMapping("/change-password")
    public RsData<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        Member member = memberService.findByEmail(userDetails.getUsername());

        String username = memberService.changePassword(member, changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword(), changePasswordRequest.getRepeatPassword());
        return RsData.successOf(username + "님의 비밀번호가 변경되었습니다.");
    }

    @GetMapping("/{username}")
    public RsData<MemberDetailDto> showMember(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        Member loginMember = memberService.findByEmail(userDetails.getUsername());
        Member pathMember = memberService.findByUsername(username);

        FollowCountDto followCountDto = followService.countFollowerAndFollowee(pathMember);
        Boolean following = followService.isFollowing(loginMember, pathMember);

        Long postCount = memberService.countPostsByUsername(username);
        return RsData.successOf(new MemberDetailDto(following, followCountDto.getFollowerCount(), followCountDto.getFolloweeCount(), postCount));
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/username")
    public RsData<String> getUsername(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        String username = memberService.findByEmail(userDetails.getUsername()).getUsername();
        return RsData.successOf(username);
    }

    @PatchMapping("/change-username")
    public RsData<String> changeUsername(@Valid @RequestBody ChangeUsernameRequest changeUsernameRequest, @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        Member member = memberService.findByEmail(userDetails.getUsername());
        LocalDateTime usernameUpdatedTime = member.getUsernameUpdatedTime();

        if (usernameUpdatedTime != null && usernameUpdatedTime.plusDays(1L).isAfter(LocalDateTime.now())) {
            return RsData.failOf("닉네임 변경 후 24시간 후에 재변경 가능합니다.");
        }
        String username = memberService.changeUsername(member,
                changeUsernameRequest.getNewUsername());
        return RsData.successOf(username + " 으로 닉네임이 변경되었습니다.");
    }
}
