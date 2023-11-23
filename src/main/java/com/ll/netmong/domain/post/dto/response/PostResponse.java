package com.ll.netmong.domain.post.dto.response;

import com.ll.netmong.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
    private String title;
    private String content;
    private String imageUrl;

    public PostResponse(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
    }
}
