package com.ll.netmong.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class MemberDetailDto {
    private boolean isFollowing;
    private Long followerCount;
    private Long followeeCount;
    private Long postCount;
}
