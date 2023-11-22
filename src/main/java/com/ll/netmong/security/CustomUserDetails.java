package com.ll.netmong.security;

import com.ll.netmong.member.dto.MemberDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private String username;                                     // DB의 P.K
    private String password;                                     // DB의 비밀번호
    private boolean accountNonLocked =true;                      // 계정 잠금 여부
    private boolean accountNonExpired =true ;                    // 사용자 계정 만료 없음
    private boolean credentialsNonExpired =true ;                // 비밀번호 만료 없음
    private boolean enabled =true;                               // 사용자 활성화 여부
    private Collection<? extends GrantedAuthority> authorities;  // 사용자 권한 목록

    // 추가로 설정하고 싶은 내용
    private String realname;                 // 사용자의 진짜 이름
    private String email;                    // 사용자 email

    public CustomUserDetails(MemberDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.realname = dto.getRealname();
        this.authorities = dto.getRoles();
    }
}