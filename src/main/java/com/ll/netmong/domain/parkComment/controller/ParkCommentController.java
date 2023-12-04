package com.ll.netmong.domain.parkComment.controller;

import com.ll.netmong.common.PageResponse;
import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import com.ll.netmong.domain.parkComment.service.ParkCommentService;
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
@RequestMapping("/api/v1/parks/comments")
public class ParkCommentController {

    private final ParkCommentService parkCommentService;

    @PostMapping("/{parkId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData<ParkCommentResponse> addParkComment(@PathVariable Long parkId, @Valid @RequestBody ParkCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        ParkCommentResponse newComment = parkCommentService.addParkComment(parkId, request, userDetails);
        return RsData.successOf(newComment);
    }

    @GetMapping("/{parkId}")
    public RsData<PageResponse<ParkCommentResponse>> getCommentsOfPark(@PathVariable Long parkId, @RequestParam(defaultValue = "1") int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<ParkCommentResponse> comments = parkCommentService.getCommentsOfPark(parkId, pageable);
        return RsData.successOf(new PageResponse<>(comments));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<ParkCommentResponse> updateComment(@PathVariable Long id, @RequestBody ParkCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        ParkCommentResponse updatedComment = parkCommentService.updateComment(id, request, userDetails);
        return RsData.successOf(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public RsData<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        parkCommentService.deleteComment(commentId, userDetails);
        return RsData.of("S-1", "삭제된 메시지입니다.");
    }

}