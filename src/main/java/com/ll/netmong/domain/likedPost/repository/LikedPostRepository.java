package com.ll.netmong.domain.likedPost.repository;

import com.ll.netmong.domain.likedPost.entity.LikedPost;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedPostRepository extends JpaRepository<LikedPost, Long> {
    int countByPostId(Long postId);

    boolean existsByMemberAndPost(Member member, Post post);

    int countLikesByPost(Post post);
}
