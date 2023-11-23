package com.ll.netmong.domain.post.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.postComment.entity.PostComment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
public class Post extends BaseEntity {
    @Column(length=100)
    private String title;
    @Column(length=100)
    private String content;
    private String imageUrl;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> comments = new ArrayList<>();

    public void addComment(PostComment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }
}
