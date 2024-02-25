package com.ll.netmong.domain.likePark.repository;

import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.park.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikedParkRepository extends JpaRepository<LikedPark, Long> {
    Boolean existsByMemberAndPark(Member member, Park park);

    Long countLikesByPark(@Param("park") Park park);

    @Query("SELECT DISTINCT lp FROM LikedPark lp JOIN FETCH lp.park WHERE lp.member = :member")
    List<LikedPark> findByMember(@Param("member") Member member);

    Optional<LikedPark> findByMemberAndPark(Member member, Park park);

    @Query("SELECT lp.park.id FROM LikedPark lp WHERE lp.member.id = :memberId")
    List<Long> findLikedParkIdsByMemberId(@Param("memberId") Long memberId);
}
