package com.ll.netmong.domain.follow.repository;

import com.ll.netmong.domain.follow.entity.Follow;
import com.ll.netmong.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(Member follower, Member followee);

}
