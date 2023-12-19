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
    private String imageUrl;
    String createDate;
    private Long likesCount;
    private Boolean isLiked;

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.content = post.getContent();
        this.imageUrl = post.getImage().getImageUrl();
        this.createDate = post.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.likesCount = post.getLikesCount();
    }

    public static PostResponse postsView (Post post) {
        return new PostResponse(post);
    }
}
