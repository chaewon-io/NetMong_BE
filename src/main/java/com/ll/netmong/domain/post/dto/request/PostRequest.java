package com.ll.netmong.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String content;

    private String category; //카테고리
    private String searchWord; //검색어
}
