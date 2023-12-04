package com.ll.netmong.domain.follow.service;

import com.ll.netmong.domain.follow.entity.Follow;
import com.ll.netmong.domain.follow.repository.FollowRepository;
import com.ll.netmong.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    @Transactional
    public Long follow(Member follower, Member followee) {
        Follow follow = Follow.builder()
                .follower(follower)
                .following(followee)
                .build();

        return followRepository.save(follow).getId();
    }

    @Transactional
    public void unfollow(Member follower, Member followee) {
        Follow follow = followRepository.findByFollowerAndFollowing(follower, followee);
        followRepository.delete(follow);
    }
}
