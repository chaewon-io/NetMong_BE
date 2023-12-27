package com.ll.netmong.member.service;

import com.ll.netmong.base.jwt.TokenDto;
import com.ll.netmong.base.jwt.TokenService;
import com.ll.netmong.domain.member.dto.EmailRequest;
import com.ll.netmong.domain.member.dto.JoinRequest;
import com.ll.netmong.domain.member.dto.LoginDto;
import com.ll.netmong.domain.member.dto.UsernameRequest;
import com.ll.netmong.domain.member.entity.AuthLevel;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.entity.ProviderTypeCode;
import com.ll.netmong.domain.member.exception.NotMatchPasswordException;
import com.ll.netmong.domain.member.service.MemberService;
import com.ll.netmong.member.mock.FakeAuthenticationManager;
import com.ll.netmong.member.mock.FakeMemberRepository;
import com.ll.netmong.member.mock.FakePasswordEncoder;
import com.ll.netmong.member.mock.FakeTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest {

    private MemberService memberService;
    private TokenService tokenService;

    String encodedPassword1;
    Member member1;

    @BeforeEach
    void init() {
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakePasswordEncoder fakePasswordEncoder = new FakePasswordEncoder();
        FakeTokenProvider fakeTokenProvider = new FakeTokenProvider("1", 84900L, "1", 90000L);
        FakeAuthenticationManager fakeAuthenticationManager = new FakeAuthenticationManager();

        this.tokenService = new TokenService(fakeTokenProvider, fakeAuthenticationManager);

        this.memberService = MemberService.builder()
                .memberRepository(fakeMemberRepository)
                .passwordEncoder(fakePasswordEncoder)
                .tokenService(tokenService)
                .build();

        encodedPassword1 = fakePasswordEncoder.encode("password1");

        member1 = Member.builder()
                .id(1L)
                .username("member1")
                .password(encodedPassword1)
                .email("aaa@aaa.com")
                .realName("realMember1")
                .authLevel(AuthLevel.MEMBER)
                .providerTypeCode(ProviderTypeCode.NETMONG)
                .build();

        fakeMemberRepository.save(member1);
    }

    @Test
    @DisplayName("findById는 id에 해당하는 멤버를 내려 준다.")
    public void MST1() throws Exception {
        //given
        //when
        Member result = memberService.findById(1L);

        //then
        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(result.getEmail()).isEqualTo("aaa@aaa.com");
    }

    @Test
    @DisplayName("createMember 는 joinRequest로 Member를 생성한다.")
    public void MST2() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUsername("user1");
        joinRequest.setPassword("password1");
        joinRequest.setEmail("email@aaa.com");
        joinRequest.setRealname("realuser1");

        //when
        Member result = memberService.createMember(joinRequest);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo(joinRequest.getEmail());
        assertThat(result.getRealName()).isEqualTo(joinRequest.getRealname());
    }

    @Test
    @DisplayName("UsernameRequest로 username이 중복되면 true를 반환한다.")
    public void MST3() throws Exception {
        //given
        UsernameRequest usernameRequest = new UsernameRequest("member1");
        //when
        boolean result = memberService.isDuplicateUsername(usernameRequest);
        //then
        assertThat(result).isTrue();

    }

    @Test
    @DisplayName("login은 loginDTO를 받아 tokenDTO를 반환한다.")
    public void loginTest() throws Exception{
        //given
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("aaa@aaa.com");
        loginDto.setPassword("password1");

        //when
        TokenDto tokenDto = memberService.login(loginDto);

        //then
        assertThat(tokenDto.getAccess_token()).isEqualTo("ACCESS_TOKEN");
        assertThat(tokenDto.getRefresh_token()).isEqualTo("REFRESH_TOKEN");
    }

    @Test
    @DisplayName("login시 비밀번호가 잘못되면 NotMatchPasswordException(잘못된 비밀번호입니다.)를 던진다.")
    public void passwordNotMatchTest() throws Exception{
        //given
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("aaa@aaa.com");
        loginDto.setPassword("passwordxxx");

        //when
        //then
        assertThatThrownBy(()->memberService.login(loginDto)).isInstanceOf(NotMatchPasswordException.class);
    }

    @Test
    @DisplayName("isDuplicateEmail은 emailRequest를 받아 중복된 이메일이 있으면 true를 반환한다.")
    public void isDuplicateEmailTest() throws Exception{
        //given
        EmailRequest emailRequest1 = new EmailRequest();
        emailRequest1.setEmail("aaa@aaa.com");

        EmailRequest emailRequest2 = new EmailRequest();
        emailRequest2.setEmail("aaa@aabb.com");

        //when
        boolean duplicateEmail = memberService.isDuplicateEmail(emailRequest1);
        boolean duplicateEmail2 = memberService.isDuplicateEmail(emailRequest2);

        //then
        assertThat(duplicateEmail).isEqualTo(true);
        assertThat(duplicateEmail2).isEqualTo(false);
    }
    
    @Test
    @DisplayName("findByUsername는 username을 받아 member를 반환한다.")
    public void findByUsernameTest() throws Exception{
        //given
        String username = "member1";

        //when
        Member testMember1 = memberService.findByUsername(username);

        //then
        assertThat(testMember1).isEqualTo(member1);
    }
}