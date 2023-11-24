package com.ll.netmong.domain.postComment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentResponse {
    private Long id;
    private String content;
    private Boolean isDeleted;
    private String username;
    private Long parentCommentId;
    private List<PostCommentResponse> childCommentsIds;
}
