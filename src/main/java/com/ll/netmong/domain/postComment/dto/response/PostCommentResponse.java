package com.ll.netmong.domain.postComment.dto.response;

import com.ll.netmong.domain.postComment.entity.PostComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static PostCommentResponse of(PostComment comment) {
        List<PostCommentResponse> childResponses = comment.getChildComments() != null
                ? comment.getChildComments().stream().map(PostCommentResponse::of).collect(Collectors.toList())
                : new ArrayList<>();

        return new PostCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getIsDeleted(),
                comment.getUsername(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                childResponses
        );
    }
}
