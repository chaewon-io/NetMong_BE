package com.ll.netmong.domain.comment.service;

import com.ll.netmong.domain.member.entity.AuthLevel;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.entity.ProviderTypeCode;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.dto.response.PostCommentResponse;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import com.ll.netmong.domain.postComment.service.PostCommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostCommentServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostCommentRepository postCommentRepository;

    @Mock
    private PostCommentServiceImpl postCommentService;

    @Mock
    private UserDetails userDetails;

    private Post post;
    private Member member;
    private PostComment postComment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        post = Post.builder()
                .title("곧 크리스마스당")
                .writer("네트멍의 익명 누군가")
                .content("네트멍 팀원들 모두 행복한 크리스마스 보내세요!")
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

        postComment = PostComment.builder()
                .post(post)
                .memberID(member)
                .content("테스트 댓글")
                .isDeleted(false)
                .isBlinded(false)
                .build();

        PostCommentResponse postCommentResponse = new PostCommentResponse();
        postCommentResponse.setId(1L);
        postCommentResponse.setContent("테스트 댓글");
        postCommentResponse.setIsDeleted(false);
        postCommentResponse.setUsername("네트멍01");
        postCommentResponse.setChildCommentsIds(new ArrayList<>());

        when(postCommentService.addPostComment(anyLong(), any(PostCommentRequest.class), any(UserDetails.class)))
                .thenReturn(postCommentResponse);
    }

    @Test
    void addPostCommentTest() {
        // given
        PostCommentRequest postCommentRequest = new PostCommentRequest();
        postCommentRequest.setContent("테스트 댓글");

        when(userDetails.getUsername()).thenReturn("네트멍01");

        // when
        PostCommentResponse response = postCommentService.addPostComment(1L, postCommentRequest, userDetails);

        // then
        assertEquals("테스트 댓글", response.getContent());
        assertEquals("네트멍01", response.getUsername());
    }

}
