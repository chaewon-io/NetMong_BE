package com.ll.netmong.domain.postComment.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.common.PageResponse;
import com.ll.netmong.domain.postComment.dto.response.PostCommentResponse;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/comment")
public class PostCommentController {

    private final PostCommentService service;

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData<PostCommentResponse> addPostComment(@PathVariable Long postId, @Valid @RequestBody PostCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        PostCommentResponse newComment = service.addPostComment(postId, request, userDetails);
        return RsData.successOf(newComment);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<PostCommentResponse> updateComment(@PathVariable Long id, @RequestBody PostCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        PostCommentResponse updatedComment = service.updateComment(id, request, userDetails);
        return RsData.successOf(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public RsData<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteComment(commentId, userDetails);
        return RsData.of("S-1", "삭제된 메시지입니다.");
    }

    @GetMapping("/{postId}")
    public RsData<PageResponse<PostCommentResponse>> getCommentsOfPost(@PathVariable Long postId, @RequestParam(defaultValue = "1") int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<PostCommentResponse> comments = service.getCommentsOfPost(postId, pageable);
        return RsData.successOf(new PageResponse<>(comments));
    }

    @PostMapping("/{commentId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData<PostComment> addReplyToComment(@PathVariable Long commentId, @RequestBody PostCommentRequest request, UserDetails userDetails) {
        PostComment childComment = service.addReplyToComment(commentId, request, userDetails);
        return RsData.successOf(childComment);
    }

    @PatchMapping("/replies/{replyId}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<PostComment> updateReply(@PathVariable Long replyId, @RequestBody PostCommentRequest request) {
        PostComment updatedReply = service.updateReply(replyId, request);
        return RsData.successOf(updatedReply);
    }

}
