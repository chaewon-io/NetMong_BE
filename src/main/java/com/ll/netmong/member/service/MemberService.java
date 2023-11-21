package com.ll.netmong.member.service;

import com.ll.netmong.member.dto.JoinRequest;
import com.ll.netmong.member.dto.UsernameRequest;
import com.ll.netmong.member.entity.AuthLevel;
import com.ll.netmong.member.entity.Member;
import com.ll.netmong.member.entity.ProviderTypeCode;
import com.ll.netmong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findById(long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Member save() {
        Member member = Member.builder().username("newUser1").build();
        return memberRepository.save(member);
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
}
