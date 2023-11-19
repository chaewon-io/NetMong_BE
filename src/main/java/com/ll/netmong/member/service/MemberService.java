package com.ll.netmong.member.service;

import com.ll.netmong.member.entity.Member;
import com.ll.netmong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Member save(){
        Member member = Member.builder().username("newUser1").build();
        return memberRepository.save(member);
    }
}
