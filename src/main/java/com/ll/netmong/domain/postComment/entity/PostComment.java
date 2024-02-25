package com.ll.netmong.domain.postComment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(nullable = false)
    private Integer depth;

    @Builder.Default
    @Column(name = "reports_count", nullable = false)
    private Integer reportCount = 0; // 신고된 횟수

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isBlinded;  // 블라인드 처리 여부

    public void updateContent(String content) {
        this.content = content;
    }

    public void setParentComment(PostComment postComment) {
        this.parentComment = postComment;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    // 자식 댓글 추가 시 깊이 계산
    public void addChildComment(PostComment childComment) {
        childComment.setParentComment(this);
        childComment.setDepth(this.getDepth() + 1);
        this.childComments.add(childComment);
    }

    public void markAsDeleted(Boolean b) {
        this.isDeleted = b;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void increaseReportCount() { // 신고 횟수 증가 메소드
        this.reportCount++;
    }

    public void checkAndBlindComment() { // 신고 횟수가 10회 이상이면 댓글 블라인드 처리
        if (this.reportCount >= 10) {
            this.isBlinded = true;
            this.content = "신고가 누적되어 블라인드 처리되었습니다.";
        }
    }

}
