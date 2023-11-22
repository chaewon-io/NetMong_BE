package com.ll.netmong.domain.park.repository;

import com.ll.netmong.domain.park.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkRepository extends JpaRepository<Park, Long> {
}