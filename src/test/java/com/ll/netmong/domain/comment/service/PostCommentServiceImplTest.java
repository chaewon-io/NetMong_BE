package com.ll.netmong.domain.comment.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.dto.response.PostCommentResponse;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostCommentServiceImplTest {

    @Autowired
    PostCommentService postCommentService;

    @MockBean
    PostRepository postRepository;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PostCommentRepository postCommentRepository;

    @Test
    @DisplayName("게시글에 댓글을 작성할 수 있다.")
    public void addPostCommentTest() {
        // given
        Long postId = 1L;
        Post post = Post.builder()
                .comments(new ArrayList<>())
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        String username = "네트멍";
        Member member = new Member();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));

        // when
        PostCommentRequest request = new PostCommentRequest();
        request.setContent("네트멍 GOAT");

        UserDetails userDetails = new User(username, "password", new ArrayList<>());

        PostComment comment = PostComment.builder()
                .post(post)
                .memberID(member)
                .content(request.getContent())
                .build();

        when(postCommentRepository.save(any(PostComment.class))).thenReturn(comment);

        PostCommentResponse response = postCommentService.addPostComment(postId, request, userDetails);

        // then
        verify(postRepository, times(1)).findById(postId);
        verify(memberRepository, times(1)).findByUsername(username);
        verify(postCommentRepository, times(1)).save(any(PostComment.class));

        assertEquals("네트멍 GOAT", response.getContent());

        System.out.println("Response: " +
                "id=" + response.getId() +
                ", content='" + response.getContent() + '\'' +
                ", isDeleted=" + response.getIsDeleted() +
                ", username='" + response.getUsername() + '\'' +
                ", parentCommentId=" + (response.getParentCommentId() != null ? response.getParentCommentId() : "null") +
                ", childComments=" + (response.getChildCommentsIds() != null ? response.getChildCommentsIds().size() : "null"));
    }

    @Test
    @DisplayName("게시글에 달린 댓글을 가져올 수 있다.")
    public void getCommentsOfPostTest() {
        // given
        Long postId = 1L;

        List<PostComment> childComments = new ArrayList<>();
        PostComment comment1 = PostComment.builder().id(1L).content("네트멍").childComments(childComments).build();
        PostComment comment2 = PostComment.builder().id(2L).content("멍멍 왈왈").childComments(childComments).build();
        List<PostComment> comments = Arrays.asList(comment1, comment2);

        // when
        when(postCommentRepository.findByPostIdAndParentCommentIsNull(postId)).thenReturn(comments);

        List<PostCommentResponse> responses = postCommentService.getCommentsOfPost(postId);

        // then
        verify(postCommentRepository, times(1)).findByPostIdAndParentCommentIsNull(postId);

        assertEquals(2, responses.size());
        assertEquals("네트멍", responses.get(0).getContent());
        assertEquals("멍멍 왈왈", responses.get(1).getContent());

        for (PostCommentResponse response : responses) {
            System.out.println("Response: " +
                    "id=" + response.getId() +
                    ", content='" + response.getContent() + '\'' +
                    ", isDeleted=" + response.getIsDeleted() +
                    ", username='" + response.getUsername() + '\'' +
                    ", parentCommentId=" + (response.getParentCommentId() != null ? response.getParentCommentId() : "null") +
                    ", childComments=" + (response.getChildCommentsIds() != null ? response.getChildCommentsIds().size() : "null"));
        }
    }

}
