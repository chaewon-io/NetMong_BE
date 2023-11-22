package com.ll.netmong.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String content;
}
