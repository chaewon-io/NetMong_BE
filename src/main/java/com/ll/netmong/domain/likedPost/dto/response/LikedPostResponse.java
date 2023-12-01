package com.ll.netmong.domain.likedPost.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedPostResponse {
    private Long postId;
    private Long likedCount;
}
