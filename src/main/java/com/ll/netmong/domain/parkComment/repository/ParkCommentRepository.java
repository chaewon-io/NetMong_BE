package com.ll.netmong.domain.parkComment.repository;

import com.ll.netmong.domain.parkComment.entity.ParkComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkCommentRepository extends JpaRepository<ParkComment, Long>  {
    Page<ParkComment> findByParkId(Long parkId, Pageable pageable);

}