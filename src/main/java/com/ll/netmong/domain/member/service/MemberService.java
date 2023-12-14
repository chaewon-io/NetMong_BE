package com.ll.netmong.domain.member.service;

import com.ll.netmong.base.jwt.TokenDto;
import com.ll.netmong.base.jwt.TokenService;
import com.ll.netmong.domain.member.dto.JoinRequest;
import com.ll.netmong.domain.member.dto.LoginDto;
import com.ll.netmong.domain.member.dto.UsernameRequest;
import com.ll.netmong.domain.member.entity.AuthLevel;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.entity.ProviderTypeCode;
import com.ll.netmong.domain.member.exception.NotMatchPasswordException;
import com.ll.netmong.domain.member.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

@Service
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Member findById(long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Member createMember(JoinRequest joinRequest) {
        Member member = Member.builder().username(joinRequest.getUsername())
                .password(joinRequest.getPassword())
                .realName(joinRequest.getRealname())
                .email(joinRequest.getEmail())
                .providerTypeCode(ProviderTypeCode.NETMONG)
                .authLevel(AuthLevel.MEMBER)
                .build();

        member.encryptPassword(passwordEncoder);

        return memberRepository.save(member);
    }

    public boolean isDuplicateUsername(UsernameRequest usernameRequest) {
        String username = usernameRequest.getUsername();
        return memberRepository.findByUsername(username).isPresent();
    }

    public TokenDto login(LoginDto loginDto) throws Exception {

        Member member = memberRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new AccountNotFoundException("아이디/비밀번호가 잘못되었습니다."));

        boolean matches = passwordEncoder.matches(loginDto.getPassword(), member.getPassword());
        if (!matches) {
            throw new NotMatchPasswordException("잘못된 비밀번호입니다.");
        }

        return tokenService.provideTokenWithLoginDto(loginDto);
    }


    public Member findByUsername(String username) throws Exception {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException("User not Found"));
    }

    @Transactional
    public String changePassword(UserDetails userDetails, String oldPassword, String newPassword) throws Exception {

        Member member = findByUsername(userDetails.getUsername());

        if (passwordEncoder.matches(oldPassword, member.getPassword())) {
            member.changePassword(newPassword);
            member.encryptPassword(passwordEncoder);
        }
        return memberRepository.save(member).getUsername();
    }

    public Long countPostsByUsername(String username) {
        return memberRepository.countPostsByMemberUsername(username);
    }
}
