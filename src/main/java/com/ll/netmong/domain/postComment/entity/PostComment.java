package com.ll.netmong.domain.postComment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @Column(length = 500)
    private String content;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isDeleted; // 삭제유무(true시 삭제된 댓글)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<PostComment> childComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberID;

    private String username;

    public void updateContent(String content) {
        this.content = content;
    }

    private void setParentComment(PostComment postComment) {
        this.parentComment = postComment;
    }

    public void addChildComment(PostComment childComment) {
        this.childComments.add(childComment);
        childComment.setParentComment(this);
    }

    public void changeIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
