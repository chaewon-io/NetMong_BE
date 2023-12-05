package com.ll.netmong.domain.likedPost.service;

import com.ll.netmong.domain.likedPost.entity.LikedPost;
import com.ll.netmong.domain.likedPost.repository.LikedPostRepository;
import com.ll.netmong.domain.member.entity.AuthLevel;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.entity.ProviderTypeCode;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LikedPostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private LikedPostRepository likedPostRepository;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private LikedPostServiceImpl likedPostService;

    private Post post;
    private Member member;
    private LikedPost likedPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        post = Post.builder()
                .title("곧 크리스마스당")
                .writer("네트멍의 익명 누군가")
                .content("연말이라닛.. 크리스마스 좋아")
                .imageUrl("http://127.0.0.1:9000/images/netmongisgoat")
                .likes(new ArrayList<>())
                .build();

        member = Member.builder()
                .authLevel(AuthLevel.MEMBER)
                .providerTypeCode(ProviderTypeCode.NETMONG)
                .username("네트멍01")
                .password("netmong1234")
                .realName("네트멍")
                .email("test@test.com")
                .build();

        likedPost = LikedPost.builder()
                .post(post)
                .member(member)
                .build();

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
    }

    @Test
    @DisplayName("게시글에 좋아요를 누를 수 있다.")
    void addLike() {
        when(likedPostRepository.existsByMemberAndPost(any(), any())).thenReturn(false);

        likedPostService.addLike(post, userDetails);

        verify(likedPostRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("누른 좋아요에 대해 취소를 할 수 있다.")
    void removeLike() {
        when(likedPostRepository.findByMemberAndPost(any(), any())).thenReturn(Optional.of(likedPost));

        likedPostService.removeLike(post, userDetails);

        verify(likedPostRepository, times(1)).delete(any());
        assertFalse(post.getLikes().contains(likedPost));
    }

    @Test
    @DisplayName("해당 게시글에 대한 좋아요 수")
    void countLikes() {
        when(likedPostRepository.countLikesByPost(any())).thenReturn(10L);

        Long likes = likedPostService.countLikes(post);

        assertEquals(10L, likes);
        verify(likedPostRepository, times(1)).countLikesByPost(any());
    }
}
