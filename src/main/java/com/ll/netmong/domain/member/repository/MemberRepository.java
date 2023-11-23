package com.ll.netmong.domain.member.repository;

import com.ll.netmong.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(long id);

    Member save(Member member);

    Optional<Member> findByUsername(String username);
}
