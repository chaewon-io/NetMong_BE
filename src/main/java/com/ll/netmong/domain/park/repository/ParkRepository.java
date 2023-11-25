package com.ll.netmong.domain.park.repository;

import com.ll.netmong.domain.park.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkRepository extends JpaRepository<Park, Long> {

    @Query("SELECT p.state FROM Park p GROUP BY p.state ORDER BY COUNT(p.state) DESC")
    List<String> findStates();

    @Query("SELECT DISTINCT p.city FROM Park p WHERE p.state = ?1 ORDER BY p.city ASC")
    List<String> findCitiesByState(String state);

    List<Park> findByLnmadrStartingWith(String stateAndCity);

}