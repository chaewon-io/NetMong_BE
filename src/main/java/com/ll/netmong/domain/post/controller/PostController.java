package com.ll.netmong.domain.post.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @Value("${spring.servlet.multipart.location}")
    private String postImagePath;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData postUpload(MultipartFile image, PostRequest postRequest) {
        String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename(); //동일한 이미지명의 이미지가 업로드되지 않도록
        try {
            Path imagePath = Path.of(postImagePath, imageName);

            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("이미지 업로드 실패");
        }
        postRequest.setImageUrl(postImagePath + imageName);

        postService.uploadPost(postRequest);

        return RsData.of("S-1", "게시물이 업로드되었습니다.");
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postDetail(@PathVariable long id) {
        PostResponse postResponse = postService.getDetail(id);

        return RsData.of("S-1", "해당 게시물의 상세 내용입니다.", postResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RsData postDelete(@PathVariable long id) {
        postService.deletePost(id);

        return RsData.of("S-1", "해당 게시물이 삭제되었습니다.");
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postUpdate(@PathVariable long id, @RequestBody PostRequest updatedPostRequest) {
        postService.updatePost(id, updatedPostRequest);

        return RsData.of("S-1", "해당 게시물이 수정되었습니다.");
    }
}
