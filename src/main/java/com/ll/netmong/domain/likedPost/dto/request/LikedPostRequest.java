package com.ll.netmong.domain.likedPost.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedPostRequest {
    private Long postId;
    private Long memberId;
}
