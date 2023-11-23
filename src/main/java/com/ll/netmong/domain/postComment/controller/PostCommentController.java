package com.ll.netmong.domain.postComment.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/post")
public class PostCommentController {

    private final PostCommentService service;

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData<PostComment> addPostComment(@PathVariable Long postId, @Valid @RequestBody PostCommentRequest request) {
        PostComment newComment = service.addPostComment(postId, request);
        return RsData.successOf(newComment);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<PostComment> updateComment(@PathVariable Long id, @RequestBody PostCommentRequest request) {
        PostComment updatedComment = service.updateComment(id, request);
        return RsData.successOf(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public RsData<String> deleteComment(@PathVariable Long commentId) {
        service.deleteComment(commentId);
        return RsData.of("S-1", "삭제된 메시지입니다.");
    }

    @GetMapping("/{postId}/list")
    public RsData<List<PostComment>> getCommentsOfPost(@PathVariable Long postId) {
        List<PostComment> comments = service.getCommentsOfPost(postId);
        return RsData.successOf(comments);
    }

    @PostMapping("/{commentId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData<PostComment> addReplyToComment(@PathVariable Long commentId, @RequestBody PostCommentRequest request) {
        PostComment childComment = service.addReplyToComment(commentId, request);
        return RsData.successOf(childComment);
    }

    @GetMapping("/{commentId}/replies")
    @ResponseStatus(HttpStatus.OK)
    public RsData<List<PostComment>> getRepliesOfComment(@PathVariable Long commentId) {
        List<PostComment> replies = service.getRepliesOfComment(commentId);
        return RsData.successOf(replies);
    }

    @PatchMapping("/replies/{replyId}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<PostComment> updateReply(@PathVariable Long replyId, @RequestBody PostCommentRequest request) {
        PostComment updatedReply = service.updateReply(replyId, request);
        return RsData.successOf(updatedReply);
    }

}
