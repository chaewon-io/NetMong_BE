package com.ll.netmong.base.security;

import com.ll.netmong.domain.member.dto.MemberDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private String username;                                     // email (실제 로그인시 사용될 이름)
    private String password;                                     // DB의 비밀번호
    private boolean accountNonLocked =true;                      // 계정 잠금 여부
    private boolean accountNonExpired =true ;                    // 사용자 계정 만료 없음
    private boolean credentialsNonExpired =true ;                // 비밀번호 만료 없음
    private boolean enabled =true;                               // 사용자 활성화 여부
    private Collection<? extends GrantedAuthority> authorities;  // 사용자 권한 목록

    // 추가로 설정하고 싶은 내용
    private String realname;                 // 사용자의 실제이름
    private String nickname;                    // username을 nickname으로 쓰기로 함

    public CustomUserDetails(MemberDto dto) {
        this.username = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getUsername();
        this.realname = dto.getRealname();
        this.authorities = dto.getRoles();
    }
}