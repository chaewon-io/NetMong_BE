package com.ll.netmong.domain.member.repository;

import com.ll.netmong.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findById(long id) {
        return memberJpaRepository.findById(id);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberJpaRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String admin) {
        return memberJpaRepository.existsByUsername(admin);
    }
}
