package com.ll.netmong.domain.post.dto.response;

import com.ll.netmong.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PostResponse {
    private Long postId;
    private String title;
    private String writer;
    private String content;
    String createDate;
    private String imageUrl;

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.content = post.getContent();
        this.createDate = post.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.imageUrl = post.getImageUrl();
    }
}
