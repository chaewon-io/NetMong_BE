package com.ll.netmong.domain.postComment.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/post")
public class PostCommentController {

    private final PostCommentService service;

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData addComment(@PathVariable long postId, @RequestBody PostCommentRequest postCommentRequest) {
        service.addPostComment(postId, postCommentRequest);
        return RsData.of("S-1", "댓글이 추가되었습니다.");
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<PostComment> updateComment(@PathVariable Long id, @RequestBody PostCommentRequest request) {
        PostComment updatedComment = service.updateComment(id, request);
        return RsData.successOf(updatedComment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<String> deleteComment(@PathVariable Long id) {
        service.deleteComment(id);
        return RsData.successOf("댓글이 삭제되었습니다.");
    }

}
