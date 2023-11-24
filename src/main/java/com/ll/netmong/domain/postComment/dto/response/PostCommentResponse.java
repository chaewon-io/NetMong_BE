package com.ll.netmong.domain.postComment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentResponse {
    private String content;
    private Boolean isDeleted;
    private String username;
}