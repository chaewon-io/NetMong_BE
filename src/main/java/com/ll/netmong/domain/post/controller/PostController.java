package com.ll.netmong.domain.post.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData postUpload(@RequestBody PostRequest postRequest) {
        postService.uploadPost(postRequest);
        return RsData.of("S-1", "게시물이 업로드되었습니다.");
    }
}
