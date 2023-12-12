package com.ll.netmong.domain.reports.service;

import com.ll.netmong.domain.member.entity.AuthLevel;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.entity.ProviderTypeCode;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.reports.dto.request.ReportRequest;
import com.ll.netmong.domain.reports.dto.response.ReportCommentResponse;
import com.ll.netmong.domain.reports.dto.response.ReportPostResponse;
import com.ll.netmong.domain.reports.entity.ReportComment;
import com.ll.netmong.domain.reports.entity.ReportPost;
import com.ll.netmong.domain.reports.repository.ReportCommentRepository;
import com.ll.netmong.domain.reports.repository.ReportPostRepository;
import com.ll.netmong.domain.reports.util.ReportType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReportServiceImplTest {

    private ReportServiceImpl reportService;
    private ReportPostRepository reportPostRepository;
    private ReportCommentRepository reportCommentRepository;

    // 테스트에 사용할 변수들
    private Member reporter;
    private Post reportedPost;
    private PostComment reportedComment;
    private ReportRequest reportRequest;

    @BeforeEach
    public void setup() {
        // Mock 객체 초기화
        reportPostRepository = mock(ReportPostRepository.class);
        reportCommentRepository = mock(ReportCommentRepository.class);

        // ReportService 객체 생성
        reportService = new ReportServiceImpl(reportPostRepository, reportCommentRepository);

        // 테스트에 사용할 변수들 초기화
        reporter = Member.builder()
                .authLevel(AuthLevel.MEMBER)
                .providerTypeCode(ProviderTypeCode.NETMONG)
                .username("jhnyuk")
                .password("netmong1234")
                .realName("네트멍")
                .email("test@test.com")
                .build();

        Member postMember = Member.builder()
                .authLevel(AuthLevel.MEMBER)
                .providerTypeCode(ProviderTypeCode.NETMONG)
                .username("foxy0716")
                .password("netmong1234")
                .realName("네트멍2")
                .email("another@test.com")
                .build();

        reportedPost = Post.builder()
                .title("곧 크리스마스당")
                .writer("네트멍의 익명 누군가")
                .content("연말이라닛.. 크리스마스 좋아")
                .imageUrl("http://127.0.0.1:9000/images/netmongisgoat")
                .likes(new ArrayList<>())
                .member(postMember)
                .build();

        reportedComment = PostComment.builder()
                .post(reportedPost)
                .content("신고 합니다.")
                .memberID(postMember)
                .build();

        reportRequest = new ReportRequest();
        reportRequest.setReportType(ReportType.SPAM);
        reportRequest.setReporterUsername(reporter.getUsername());
        reportRequest.setContent("신고 합니다.");
    }

    @Test
    @Order(1)
    @DisplayName("게시글 신고")
    public void testReportPost() {
        when(reportPostRepository.existsByReporterAndReportedPost(reporter, reportedPost)).thenReturn(false);

        ReportPostResponse response = reportService.reportPost(reportRequest, reportedPost, reporter);

        assertNotNull(response);
        verify(reportPostRepository, times(1)).existsByReporterAndReportedPost(reporter, reportedPost);
        verify(reportPostRepository, times(1)).save(any(ReportPost.class));
    }

    @Test
    @Order(3)
    @DisplayName("댓글 신고")
    public void testReportComment() {
        when(reportCommentRepository.existsByReporterAndReportedComment(reporter, reportedComment)).thenReturn(false);

        ReportCommentResponse response = reportService.reportComment(reportRequest, reportedComment, reporter);

        assertNotNull(response);
        verify(reportCommentRepository, times(1)).existsByReporterAndReportedComment(reporter, reportedComment);
        verify(reportCommentRepository, times(1)).save(any(ReportComment.class));
    }

    @Test
    @Order(2)
    @DisplayName("신고된 게시글 조회")
    public void testGetReportedPosts() {
        reportService.getReportedPosts();

        verify(reportPostRepository, times(1)).findAll();
    }

    @Test
    @Order(4)
    @DisplayName("신고된 댓글 조회")
    public void testGetReportedComments() {
        reportService.getReportedComments();

        verify(reportCommentRepository, times(1)).findAll();
    }

}
