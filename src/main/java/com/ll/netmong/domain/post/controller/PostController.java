package com.ll.netmong.domain.post.controller;

import com.ll.netmong.common.PageResponse;
import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.hashtag.service.HashtagService;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.dto.response.PostResponse;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.service.PostService;
import com.ll.netmong.domain.postHashtag.service.PostHashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final HashtagService hashtagService;
    private final PostHashtagService postHashtagService;

    @Value("${spring.servlet.multipart.location}")
    private String postImagePath;

    @Value("${domain}")
    String  domain;

    @GetMapping("/hashtagSearch")
    public RsData searchByHashtag(@RequestParam String hashtag, @RequestParam(defaultValue = "1") int page) {
        Pageable pageRequest = PageRequest.of(page - 1, 5);
        Page<PostResponse> hashtagSearch = postService.searchPostsByHashtag(hashtag, pageRequest);

        return RsData.successOf(new PageResponse<>(hashtagSearch));
    }

    @GetMapping("/categorySearch")
    public RsData postsSearch(@RequestParam String category, @RequestParam String searchWord, @RequestParam(defaultValue = "1") int page) {
        Pageable pageRequest = PageRequest.of(page - 1, 5);
        Page<PostResponse> categorySearch = postService.searchPostsByCategory(category, searchWord, pageRequest);

        return RsData.successOf(new PageResponse<>(categorySearch));
    }

    @GetMapping("/view")
    public RsData postsViewByPage(@RequestParam(defaultValue = "1") int page) {
        Pageable pageRequest = PageRequest.of(page - 1, 5);
        Page<PostResponse> postsView = postService.viewPostsByPage(pageRequest);

        return RsData.successOf(new PageResponse<>(postsView));
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public RsData postUpload(@AuthenticationPrincipal UserDetails userDetails, MultipartFile image, PostRequest postRequest) throws Exception {
        Member foundMember = memberService.findByUsername(userDetails.getUsername());

        Post createdPost = postService.uploadPostWithImage(postRequest, image, foundMember);
        PostResponse postResponse = new PostResponse(createdPost);
        hashtagService.saveHashtag(postRequest, createdPost);

        return RsData.of("S-1", "게시물이 업로드되었습니다.", postResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postDetail(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        PostResponse postResponse = postService.getDetail(id, userDetails);

        return RsData.of("S-1", "해당 게시물의 상세 내용입니다.", postResponse);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RsData postDelete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long postId) throws Exception {
        Member foundMember = memberService.findByUsername(userDetails.getUsername());
        String foundUsername = foundMember.getUsername();

        postService.deletePost(postId, foundUsername);
        postHashtagService.deleteHashtag(postId);

        return RsData.of("S-1", "해당 게시물이 삭제되었습니다.", "삭제 성공");
    }

    @PatchMapping ("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData postUpdate(@AuthenticationPrincipal UserDetails userDetails, MultipartFile image, @PathVariable Long id, PostRequest updatedPostRequest) throws Exception {
        Member foundMember = memberService.findByUsername(userDetails.getUsername());
        String foundUsername = foundMember.getUsername();

        //saveImage(image, updatedPostRequest);

        postService.updatePost(id, updatedPostRequest, foundUsername);
        postHashtagService.updateHashtag(id, updatedPostRequest);

        return RsData.of("S-1", "해당 게시물이 수정되었습니다.", updatedPostRequest);
    }

//    public void saveImage(MultipartFile image, PostRequest postRequest) {
//        String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename(); //동일한 이미지명의 이미지가 업로드되지 않도록
//
//        try {
//            Path imagePath = Path.of(postImagePath, imageName);
//
//            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException ex) {
//            throw new RuntimeException("이미지 업로드 실패");
//        }
//
//        postRequest.setImageUrl(domain + "/" + postImagePath + imageName);
//    }

    @GetMapping("/my-posts")
    @ResponseStatus(HttpStatus.OK)
    public RsData<PageResponse<PostResponse>> viewMyPage(@RequestParam(defaultValue = "1") int page, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        String username = userDetails.getUsername();
        Long memberId = memberService.findByUsername(username).getId();

        Pageable pageRequest = PageRequest.of(page - 1, 5);
        Page<PostResponse> myPosts = postService.viewPostsByMemberId(memberId, pageRequest);
        return RsData.successOf(new PageResponse<>(myPosts));
    }

    @GetMapping("/member/{username}")
    @ResponseStatus(HttpStatus.OK)
    public RsData<PageResponse<PostResponse>> viewMemberWrittenPosts(@PathVariable String username, @RequestParam(defaultValue = "1") int page) throws Exception {

        Long memberId = memberService.findByUsername(username).getId();

        Pageable pageRequest = PageRequest.of(page - 1, 5);
        Page<PostResponse> myPosts = postService.viewPostsByMemberId(memberId, pageRequest);
        return RsData.successOf(new PageResponse<>(myPosts));
    }
}
