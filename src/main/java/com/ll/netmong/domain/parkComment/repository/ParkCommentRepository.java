package com.ll.netmong.domain.parkComment.repository;

import com.ll.netmong.domain.parkComment.entity.ParkComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkCommentRepository extends JpaRepository<ParkComment, Long>  {
    List<ParkComment> findByParkId(Long parkId);

}