package com.ll.netmong.domain.likePark.repository;

import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.park.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedParkRepository extends JpaRepository<LikedPark, Long> {
    Boolean existsByMemberAndPark(Member member, Park park);

    Long countLikesByPark(Park park);
}
