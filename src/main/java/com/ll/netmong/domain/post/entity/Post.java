package com.ll.netmong.domain.post.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.likedPost.entity.LikedPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE post SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class Post extends BaseEntity {
    @Column(length=100)
    private String title;
    private String writer;
    @Column(length=100)
    private String content;
    private String imageUrl;

    @Column(name = "deleted_at")
    private String deleteDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //포스트 수정
    public void update(String content, String imageUrl) {
        this.content = content;
        this.imageUrl = imageUrl;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> comments = new ArrayList<>();

    public void addComment(PostComment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    @Builder.Default
    @Column(name = "likes_count", nullable = false)
    private Long likesCount = 0L;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<LikedPost> likes = new ArrayList<>();

    public void addLike(LikedPost like) {
        this.likes.add(like);
        like.setPost(this);
        this.likesCount++;  // 좋아요 수 증가
    }

    public void removeLike(LikedPost like) {
        this.likes.remove(like);
        like.setPost(null);
        this.likesCount--;  // 좋아요 수 감소
    }

}
