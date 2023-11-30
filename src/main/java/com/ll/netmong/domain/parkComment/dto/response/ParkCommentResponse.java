package com.ll.netmong.domain.parkComment.dto.response;

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

}
