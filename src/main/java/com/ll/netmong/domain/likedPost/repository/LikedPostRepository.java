package com.ll.netmong.domain.likedPost.repository;

import com.ll.netmong.domain.likedPost.entity.LikedPost;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikedPostRepository extends JpaRepository<LikedPost, Long> {
    Boolean existsByMemberAndPost(Member member, Post post);

    Long countLikesByPost(Post post);

    Optional<LikedPost> findByMemberAndPost(Member member, Post post);
}
