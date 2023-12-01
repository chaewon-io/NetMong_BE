package com.ll.netmong.domain.likedPost.service;

import com.ll.netmong.domain.likedPost.exception.DuplicateLikeException;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.postComment.exception.DataNotFoundException;
import com.ll.netmong.domain.likedPost.dto.request.LikedPostRequest;
import com.ll.netmong.domain.likedPost.dto.response.LikedPostResponse;
import com.ll.netmong.domain.likedPost.entity.LikedPost;
import com.ll.netmong.domain.likedPost.repository.LikedPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikedPostServiceImpl implements LikedPostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikedPostRepository likedPostRepository;

    @Override
    @Transactional
    public void addLike(Post post, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = getMemberById(userDetails);

        // 특정 게시물에 대한 좋아요만 조회
        boolean hasAlreadyLiked = likedPostRepository.existsByMemberAndPost(member, post);

        if (hasAlreadyLiked) {
            throw new DuplicateLikeException("이미 좋아요를 누른 게시물입니다.");
        }

        LikedPost like = LikedPost.builder()
                .post(post)
                .member(member)
                .build();

        post.addLike(like);
        likedPostRepository.save(like);
    }

    @Override
    @Transactional
    public void removeLike(Post post, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = getMemberById(userDetails);

        // 특정 게시물에 대한 좋아요만 조회
        LikedPost likedPost = likedPostRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물에 좋아요를 누르지 않았습니다."));

        post.removeLike(likedPost);
        likedPostRepository.delete(likedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public int countLikes(Post post) {
        return likedPostRepository.countLikesByPost(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 게시물을 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(@AuthenticationPrincipal UserDetails userDetails) {
        return memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
