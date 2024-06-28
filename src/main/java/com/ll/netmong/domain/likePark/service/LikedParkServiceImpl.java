package com.ll.netmong.domain.likePark.service;

import com.ll.netmong.domain.likePark.dto.response.LikedParkResponse;
import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.likePark.exception.DuplicateLikedParkException;
import com.ll.netmong.domain.likePark.exception.LikedParkNotFoundException;
import com.ll.netmong.domain.likePark.repository.LikedParkRepository;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.exception.MemberNotFoundException;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.dto.response.ParkResponse;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.exception.ParkNotFoundException;
import com.ll.netmong.domain.park.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikedParkServiceImpl implements LikedParkService {

    private final ParkRepository parkRepository;
    private final MemberRepository memberRepository;
    private final LikedParkRepository likedParkRepository;

    @Override
    @Transactional
    public void addLikeToPark(Long parkId, UserDetails userDetails) {
        try {
            Park park = getParkById(parkId);
            Member member = getMemberById(userDetails);

            boolean hasAlreadyLiked = likedParkRepository.existsByMemberAndPark(member, park);

            if (hasAlreadyLiked) {
                throw new DuplicateLikedParkException("이미 좋아요를 누른 공원입니다.");
            }

            LikedPark like = LikedParkResponse.of(park, member);

            park.addLikeToPark(like);
            likedParkRepository.save(like);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("다른 사용자가 동시에 좋아요를 눌렀습니다. 다시 시도해주세요.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLikesToPark(Long parkId) {
        return likedParkRepository.countLikesByParkId(parkId);
    }

    @Override
    @Transactional
    public void removeLikeFromPark(Long parkId, UserDetails userDetails) {
        try {
            Park park = getParkById(parkId);
            Member member = getMemberById(userDetails);
            LikedPark likedPark = getLikedParkByMemberAndPark(member, park);

            park.removeLikeFromPark(likedPark);
            likedParkRepository.delete(likedPark);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("다른 사용자가 동시에 좋아요를 삭제했습니다. 다시 시도해주세요.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkResponse> getLikedParksForMember(UserDetails userDetails) {
        Member member = getMemberById(userDetails);
        List<LikedPark> likedParks = likedParkRepository.findByMember(member);
        return likedParks.stream()
                .map(likedPark -> ParkResponse.of(likedPark.getPark(), true))
                .collect(Collectors.toList());
    }

    private Member getMemberById(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Park getParkById(Long parkId) {
        return parkRepository.findWithOptimisticLockById(parkId)
                .orElseThrow(() -> new ParkNotFoundException("해당하는 공원을 찾을 수 없습니다."));
    }

    private LikedPark getLikedParkByMemberAndPark(Member member, Park park) {
        return likedParkRepository.findByMemberAndPark(member, park)
                .orElseThrow(() -> new LikedParkNotFoundException("해당 공원에 좋아요를 누르지 않았습니다."));
    }

}
