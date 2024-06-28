package com.ll.netmong.domain.parkComment.dto.response;

import com.ll.netmong.domain.parkComment.entity.ParkComment;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkCommentResponse {
    private Long id;
    private String content;
    private Boolean isDeleted;
    private String username;

    public static ParkCommentResponse from(ParkComment parkComment) {
        return ParkCommentResponse.builder()
                .id(parkComment.getId())
                .content(parkComment.getContent())
                .isDeleted(parkComment.getIsDeleted())
                .username(parkComment.getUsername())
                .build();
    }

}