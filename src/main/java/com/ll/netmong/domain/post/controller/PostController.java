package com.ll.netmong.domain.post.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @Value("${spring.servlet.multipart.location}")
    private String postImagePath;

    @GetMapping("/view")
    @ResponseStatus(HttpStatus.OK)
    public RsData postView() {
        List<PostResponse> postViewAll = postService.getViewAll();

        return RsData.of("S-1", "전체 게시글이 조회되었습니다.", postViewAll);
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData postUpload(@AuthenticationPrincipal UserDetails userDetails, MultipartFile image, PostRequest postRequest) throws Exception {
        Member foundMember = memberService.findByUsername(userDetails.getUsername());
        String foundUsername = foundMember.getUsername();

        saveImage(image, postRequest);

        Post createdPost = postService.uploadPost(postRequest, foundMember, foundUsername);
        PostResponse postResponse = new PostResponse(createdPost);

        return RsData.of("S-1", "게시물이 업로드되었습니다.", postResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postDetail(@PathVariable long id) {
        PostResponse postResponse = postService.getDetail(id);

        return RsData.of("S-1", "해당 게시물의 상세 내용입니다.", postResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RsData postDelete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) throws Exception {
        Member foundMember = memberService.findByUsername(userDetails.getUsername());
        String foundUsername = foundMember.getUsername();

        postService.deletePost(id, foundUsername);

        return RsData.of("S-1", "해당 게시물이 삭제되었습니다.");
    }

    @PatchMapping ("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postUpdate(@AuthenticationPrincipal UserDetails userDetails, MultipartFile image, @PathVariable Long id, PostRequest updatedPostRequest) throws Exception {
        Member foundMember = memberService.findByUsername(userDetails.getUsername());
        String foundUsername = foundMember.getUsername();

        saveImage(image, updatedPostRequest);

        postService.updatePost(id, updatedPostRequest, foundUsername);

        return RsData.of("S-1", "해당 게시물이 수정되었습니다.", updatedPostRequest);
    }

    public void saveImage(MultipartFile image, PostRequest postRequest) {
        String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename(); //동일한 이미지명의 이미지가 업로드되지 않도록

        try {
            Path imagePath = Path.of(postImagePath, imageName);

            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("이미지 업로드 실패");
        }

        postRequest.setImageUrl(postImagePath + imageName);
    }
}
