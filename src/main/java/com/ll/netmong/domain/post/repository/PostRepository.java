package com.ll.netmong.domain.post.repository;

import com.ll.netmong.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
