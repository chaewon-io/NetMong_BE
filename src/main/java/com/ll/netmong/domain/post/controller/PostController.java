package com.ll.netmong.domain.post.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    String  imagesUploadPath = "images";
    String  domain = "http://127.0.0.1:9000";

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData postUpload(MultipartFile image, PostRequest postRequest) {
        String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename(); //동일한 이미지명의 이미지가 업로드되지 않도록

        try {
            Path imagePath = Path.of(imagesUploadPath, imageName);

            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to upload image");
        }

        postRequest.setImageUrl(domain + "/" + imagesUploadPath + "/" + imageName);

        postService.uploadPost(image, postRequest);

        return RsData.of("S-1", "게시물이 업로드되었습니다.");
    }

    @GetMapping("/detail/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postDetail(@PathVariable long id) {
        PostResponse postResponse = postService.getDetail(id);

        return RsData.of("S-1", "해당 게시물의 상세 내용입니다.", postResponse);
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RsData postDelete(@PathVariable long id) {
        postService.deletePost(id);

        return RsData.of("S-1", "해당 게시물이 삭제되었습니다.");
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postUpdate(@PathVariable long id, @RequestBody PostRequest updatedPostRequest) {
        postService.updatePost(id, updatedPostRequest);

        return RsData.of("S-1", "해당 게시물이 수정되었습니다.");
    }
}
