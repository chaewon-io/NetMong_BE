package com.ll.netmong.domain.likePark.service;

import com.ll.netmong.domain.likePark.entity.LikedPark;
import com.ll.netmong.domain.likePark.repository.LikedParkRepository;
import com.ll.netmong.domain.likedPost.exception.DuplicateLikeException;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.park.entity.Park;
import com.ll.netmong.domain.park.repository.ParkRepository;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikedParkServiceImpl implements LikedParkService {

    private final ParkRepository parkRepository;
    private final MemberRepository memberRepository;
    private final LikedParkRepository likedParkRepository;

    @Override
    @Transactional
    public void addLikeToPark(Park park, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = getMemberById(userDetails);

            boolean hasAlreadyLiked = likedParkRepository.existsByMemberAndPark(member, park);

            if (hasAlreadyLiked) {
                throw new DuplicateLikeException("이미 좋아요를 누른 공원입니다.");
            }

            LikedPark like = LikedPark.createLikedPark(park, member);

            park.addLikeToPark(like);
            likedParkRepository.save(like);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("다른 사용자가 동시에 좋아요를 눌렀습니다. 다시 시도해주세요.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Park getParkById(Long parkId) {
        Park park = parkRepository.findWithOptimisticLockById(parkId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 공원을 찾을 수 없습니다."));
        return park;
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(@AuthenticationPrincipal UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLikesToPark(Park park) {
        return likedParkRepository.countLikesByPark(park);
    }

    @Override
    @Transactional
    public void removeLikeFromPark(Park park, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = getMemberById(userDetails);

            LikedPark likedPark = likedParkRepository.findByMemberAndPark(member, park)
                    .orElseThrow(() -> new IllegalArgumentException("해당 공원에 좋아요를 누르지 않았습니다."));

            park.removeLikeFromPark(likedPark);
            likedParkRepository.delete(likedPark);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("다른 사용자가 동시에 좋아요를 삭제했습니다. 다시 시도해주세요.");
        }
    }
}
