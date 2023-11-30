package com.ll.netmong.domain.parkComment.service;

import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ParkCommentService {
    ParkCommentResponse addParkComment(Long parkId, ParkCommentRequest parkCommentRequest, UserDetails userDetails);
    List<ParkCommentResponse> getCommentsOfPark(Long parkId);
    ParkCommentResponse updateComment(Long commentId, ParkCommentRequest request, UserDetails userDetails);
    void deleteComment(Long commentId, UserDetails userDetails);


}
