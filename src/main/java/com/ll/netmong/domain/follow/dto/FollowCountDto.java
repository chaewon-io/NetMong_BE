package com.ll.netmong.domain.follow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowCountDto {
    private long followerCount;
    private long followeeCount;

    public FollowCountDto(long followerCount, long followeeCount) {
        this.followerCount = followerCount;
        this.followeeCount = followeeCount;
    }
}
