package com.ll.netmong.domain.postComment.repository;

import com.ll.netmong.domain.postComment.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostIdAndParentCommentIsNull(Long postId);
}
