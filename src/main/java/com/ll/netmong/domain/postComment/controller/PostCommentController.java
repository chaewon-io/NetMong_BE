package com.ll.netmong.domain.postComment.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class PostCommentController {

    private final PostCommentService service;

    @PostMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData addComment(@PathVariable long postId, @RequestBody PostCommentRequest postCommentRequest) {
        service.addPostComment(postId, postCommentRequest);
        return RsData.of("S-1", "댓글이 추가되었습니다.");
    }

}
