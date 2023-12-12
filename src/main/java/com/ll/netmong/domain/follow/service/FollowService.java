package com.ll.netmong.domain.follow.service;

import com.ll.netmong.domain.follow.dto.FollowCountDto;
import com.ll.netmong.domain.follow.entity.Follow;
import com.ll.netmong.domain.follow.exception.NotFollowedException;
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
                .followee(followee)
                .build();

        return followRepository.save(follow).getId();
    }

    @Transactional
    public void unfollow(Member follower, Member followee) {
        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee).orElseThrow(() ->
                new NotFollowedException("현재 팔로우 중인 상태가 아닙니다.")
        );
        followRepository.delete(follow);
    }

    public FollowCountDto countFollowerAndFollowee(Member member) {
        long countByFollower = followRepository.countByFollower(member);
        long countByFollowee = followRepository.countByFollowee(member);

        return new FollowCountDto(countByFollower, countByFollowee);
    }

    public Boolean isFollowing(Member loginMember, Member foundMember) {
        return followRepository.findByFollowerAndFollowee(loginMember, foundMember).isPresent();
    }

    public Follow findById(long id) {
        return followRepository.findById(id).orElseThrow(() -> new NotFollowedException("팔로우 중인 상태가 아닙니다."));
    }
}
