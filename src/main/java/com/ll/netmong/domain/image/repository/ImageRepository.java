package com.ll.netmong.domain.image.repository;

import com.ll.netmong.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
