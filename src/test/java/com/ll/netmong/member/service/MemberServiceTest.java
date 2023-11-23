package com.ll.netmong.member.service;

import com.ll.netmong.jwt.TokenService;
import com.ll.netmong.member.dto.JoinRequest;
import com.ll.netmong.member.dto.UsernameRequest;
import com.ll.netmong.member.entity.AuthLevel;
import com.ll.netmong.member.entity.Member;
import com.ll.netmong.member.entity.ProviderTypeCode;
import com.ll.netmong.member.mock.FakeAuthenticationManagerBuilder;
import com.ll.netmong.member.mock.FakeMemberRepository;
import com.ll.netmong.member.mock.FakePasswordEncoder;
import com.ll.netmong.member.mock.FakeTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceTest {

    private MemberService memberService;
    private TokenService tokenService;

    @BeforeEach
    void init() {
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakePasswordEncoder fakePasswordEncoder = new FakePasswordEncoder();
        FakeTokenProvider fakeTokenProvider = new FakeTokenProvider("1", 84900L, "1", 90000L);
        FakeAuthenticationManagerBuilder fakeAuthenticationManagerBuilder = new FakeAuthenticationManagerBuilder();

        this.tokenService = new TokenService(fakeTokenProvider, fakeAuthenticationManagerBuilder);

        this.memberService = MemberService.builder()
                .memberRepository(fakeMemberRepository)
                .passwordEncoder(fakePasswordEncoder)
                .tokenService(tokenService)
                .build();

        Member member1 = Member.builder()
                .id(1L)
                .username("member1")
                .password("encodedpassword1")
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
        JoinRequest joinRequest = JoinRequest.builder()
                .username("user1")
                .password("password1")
                .email("email@aaa.com")
                .realname("realuser1")
                .build();
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

}