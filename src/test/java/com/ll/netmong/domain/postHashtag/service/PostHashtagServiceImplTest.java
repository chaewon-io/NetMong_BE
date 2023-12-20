package com.ll.netmong.domain.postHashtag.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.repository.MemberRepository;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.post.repository.PostRepository;
import com.ll.netmong.domain.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostHashtagServiceImplTest {
    String username;
    Member member;
    Post post;
    MockMultipartFile image;

    @Autowired
    PostService postService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private  PostHashtagService postHashtagService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

//    @BeforeEach
//    void setUp() {
//        username = "username150";
//
//        JoinRequest joinRequest = new JoinRequest();
//        joinRequest.setUsername(username);
//        joinRequest.setPassword("password150");
//        joinRequest.setRealname("realname150");
//        joinRequest.setEmail("name150@naver.com");
//        member = memberService.createMember(joinRequest);
//        memberRepository.save(member);
//
//        PostRequest postRequest = new PostRequest();
//        postRequest.setTitle("테스트");
//        postRequest.setContent("#테스트 작성 중 #실행");
//
//        image = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
//    }
//
//    @Test
//    @DisplayName("postId를 받아 해당 게시물의 해시태그를 삭제한다.")
//    void testDeleteHashtag() {
//        //given
//        Post savedPost = postRepository.save(Post.builder()
//                .title("테스트")
//                .member(member)
//                .writer(username)
//                .content("#테스트 작성 중")
//                .build());
//
//        Post foundPost = postRepository.findById(post.getId())
//                .orElseThrow(() -> new EntityNotFoundException("포스트를 찾을 수 없습니다."));
//
//        //when
//        postHashtagService.deleteHashtag(savedPost.getId());
//
//        //then
//        Assertions.assertThat(foundPost.getStatus()).isEqualTo("N");
//    }
//
//    @Test
//    @DisplayName("postId와 postRequest를 받아 해당 게시물의 해시태그를 수정(추가, 삭제)한다.")
//    void testUpdateHashtag() {
//        //given
//        Post post = Post.builder()
//                .title("테스트")
//                .member(member)
//                .writer(username)
//                .content("#테스트 작성 중")
//                .build();
//        List<String> originNames = post.getNames().stream()
//                .map(postHashtag -> postHashtag.getHashtag().getName())
//                .collect(Collectors.toList());
//
//        UpdatePostRequest updatedPostRequest = new UpdatePostRequest();
//        updatedPostRequest.setTitle("멍멍");
//        updatedPostRequest.setContent("#오늘 강아지 #멍멍");
//
//        //when
//        postHashtagService.updateHashtag(post.getId(), updatedPostRequest);
//
//        //then
//        Assertions.assertThat(originNames.get(0)).isEqualTo("오늘");
//        Assertions.assertThat(originNames.get(1)).isEqualTo("멍멍");
//    }
}