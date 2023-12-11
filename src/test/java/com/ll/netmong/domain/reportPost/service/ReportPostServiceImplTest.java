package com.ll.netmong.domain.reportPost.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.reportPost.dto.request.ReportPostRequest;
import com.ll.netmong.domain.reportPost.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reportPost.repository.ReportPostRepository;
import com.ll.netmong.domain.reports.util.ReportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ReportPostRepository reportPostRepository;

    @InjectMocks
    private ReportPostServiceImpl reportPostService;

    private Member reporter;
    private Member author;
    private Post reportedPost;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        reporter = Member.builder()
                .id(1L)
                .username("신고자")
                .build();

        author = Member.builder()
                .id(2L)
                .username("게시글 작성자")
                .build();

        reportedPost = Post.builder()
                .id(1L)
                .member(author)
                .build();

        userDetails = new User("신고자", "", new ArrayList<>());

        when(memberRepository.findByUsername("신고자")).thenReturn(Optional.of(reporter));
        when(postRepository.findById(1L)).thenReturn(Optional.of(reportedPost));
    }

    @Test
    @DisplayName("유저는 게시글을 신고할 수 있다.")
    public void reportPostTest() {
        // given
        ReportPostRequest request = new ReportPostRequest();
        request.setReportType(ReportType.SPAM);
        request.setContent("스팸 게시물 신고합니다.");

        // when
        ReportPostResponse response = reportPostService.reportPost(1L, request, userDetails);

        // then
        assertEquals(1L, response.getReportedPostId());
        assertEquals(ReportType.SPAM, response.getReportType());
        assertEquals("스팸 게시물 신고합니다.", response.getContent());
    }
}
