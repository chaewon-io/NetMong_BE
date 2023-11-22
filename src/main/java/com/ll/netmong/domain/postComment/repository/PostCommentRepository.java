package com.ll.netmong.domain.postComment.repository;

import com.ll.netmong.domain.postComment.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Transactional(readOnly = true)
    @Modifying
    @Query(value = "ALTER TABLE post_comment AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();
}
