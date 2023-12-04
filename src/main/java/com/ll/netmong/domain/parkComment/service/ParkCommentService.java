package com.ll.netmong.domain.parkComment.service;

import com.ll.netmong.domain.parkComment.dto.request.ParkCommentRequest;
import com.ll.netmong.domain.parkComment.dto.response.ParkCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface ParkCommentService {
    ParkCommentResponse addParkComment(Long parkId, ParkCommentRequest parkCommentRequest, UserDetails userDetails);
    Page<ParkCommentResponse> getCommentsOfPark(Long parkId, Pageable pageable);
    ParkCommentResponse updateComment(Long commentId, ParkCommentRequest request, UserDetails userDetails);
    void deleteComment(Long commentId, UserDetails userDetails);

}
