package com.ll.netmong.domain.comment.service;

import com.ll.netmong.domain.postComment.dto.request.PostCommentRequest;
import com.ll.netmong.domain.postComment.entity.PostComment;
import com.ll.netmong.domain.postComment.repository.PostCommentRepository;
import com.ll.netmong.domain.postComment.service.PostCommentService;
import com.ll.netmong.post.Post;
import com.ll.netmong.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostCommentServiceImplTest {

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Test
    @DisplayName("게시글에 댓글을 작성할 수 있다.")
    void addPostComment() {
        // given
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        postRepository.save(post);

        PostCommentRequest request = new PostCommentRequest();
        request.setContent("Test Comment");

        // when
        PostComment comment = postCommentService.addPostComment(post.getId(), request);

        // then
        assertNotNull(comment);
        assertEquals(request.getContent(), comment.getContent());
    }

    @Test
    @DisplayName("댓글을 수정할 수 있다.")
    void updateComment() {
        // given
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        postRepository.save(post);

        PostComment comment = PostComment.builder()
                .content("Original Comment")
                .post(post)
                .build();
        postCommentRepository.save(comment);

        PostCommentRequest request = new PostCommentRequest();
        request.setContent("Updated Comment");

        // when
        PostComment updatedComment = postCommentService.updateComment(comment.getId(), request);

        // then
        assertNotNull(updatedComment);
        assertEquals(request.getContent(), updatedComment.getContent());
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    void deleteComment() {
        // given
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        postRepository.save(post);

        PostComment comment = PostComment.builder()
                .content("To be Deleted Comment")
                .post(post)
                .build();
        postCommentRepository.save(comment);

        // when
        postCommentService.deleteComment(comment.getId());

        // then
        assertFalse(postCommentRepository.findById(comment.getId()).isPresent());
    }

    @Test
    @DisplayName("게시글에 달린 댓글을 가져올 수 있다.")
    void getCommentsOfPost() {
        // given
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .comments(new ArrayList<>())
                .build();
        postRepository.save(post);

        PostComment comment1 = PostComment.builder()
                .content("First Comment")
                .build();

        PostComment comment2 = PostComment.builder()
                .content("Second Comment")
                .build();

        post.addComment(comment1);
        post.addComment(comment2);
        postCommentRepository.saveAll(Arrays.asList(comment1, comment2));

// when
        List<PostComment> comments = postCommentService.getCommentsOfPost(post.getId());

// then
        assertNotNull(comments);
        assertEquals(2, comments.size());
    }
}
