package com.ll.netmong.domain.postComment.repository;

import com.ll.netmong.domain.postComment.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Page<PostComment> findByPostIdAndParentCommentIsNull(Long postId, Pageable pageable);
}
