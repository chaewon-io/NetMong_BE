package com.ll.netmong.domain.parkComment.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import com.ll.netmong.domain.parkComment.service.ParkCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public RsData<List<ParkCommentResponse>> getCommentsOfPost(@PathVariable Long parkId) {
        List<ParkCommentResponse> comments = parkCommentService.getCommentsOfPark(parkId);
        return RsData.successOf(comments);
    }

}