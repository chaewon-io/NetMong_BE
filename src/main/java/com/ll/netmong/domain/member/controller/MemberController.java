package com.ll.netmong.domain.member.controller;

import com.ll.netmong.base.jwt.TokenDto;
import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.member.dto.ChangePasswordRequest;
import com.ll.netmong.domain.member.dto.JoinRequest;
import com.ll.netmong.domain.member.dto.LoginDto;
import com.ll.netmong.domain.member.dto.UsernameRequest;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CartService cartService;

    @GetMapping("/find")
    public Member findMember() {
        return memberService.findById(1L);
    }

    @PostMapping("/join")
    public RsData<String> join(@Valid @RequestBody JoinRequest joinRequest) throws Exception {

        String username = memberService.createMember(joinRequest).getUsername();
        cartService.createCart(memberService.findByUsername(joinRequest.getUsername()));

        return RsData.successOf(username);
    }

    @PostMapping("/dup-username")
    public RsData checkDupUsername(@Valid @RequestBody UsernameRequest usernameRequest) {

        if (memberService.isDuplicateUsername(usernameRequest)) {
            return RsData.of("F-1", "이미 중복된 아이디가 있습니다.");
        }

        return RsData.of("S-1", "사용가능한 아이디입니다.");
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
        String username = memberService.changePassword(userDetails, changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword());
        return RsData.successOf(username + "님의 비밀번호가 변경되었습니다.");
    }
}
