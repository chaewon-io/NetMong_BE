package com.ll.netmong.domain.post.repository;

import com.ll.netmong.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    Page<Post> findByMemberIdAndDeleteDateIsNullOrderByCreateDateDesc(@Param("memberId") Long memberId, Pageable pageable);

}
