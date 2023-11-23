package com.ll.netmong.domain.postComment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.post.Post;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<PostComment> childComments = new ArrayList<>();

    public void update(String content) {
        this.content = content;
    }

    public void addChildComment(PostComment childComment) {
        this.childComments.add(childComment);
        childComment.setParentComment(this);
    }

}
