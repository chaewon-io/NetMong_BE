package com.ll.netmong.domain.member.repository;

import com.ll.netmong.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Boolean existsByUsername(String admin);

    @Query("select count(p) from Post p where p.member= (select m from Member m where m.username = :username)")
    Long countPostsByMemberUsername(@Param("username") String username);
}
