package com.ll.netmong.domain.likedPost.repository;

import com.ll.netmong.domain.likedPost.entity.LikedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedPostRepository extends JpaRepository<LikedPost, Long> {
    int countByPostId(Long postId);
}
