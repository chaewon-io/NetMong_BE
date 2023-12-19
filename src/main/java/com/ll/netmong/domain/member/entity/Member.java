package com.ll.netmong.domain.member.entity;

import com.ll.netmong.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private AuthLevel authLevel;
    @Enumerated(EnumType.STRING)
    private ProviderTypeCode providerTypeCode;

    @Column(unique = true)
    private String email;
    private String password;

    @Column(unique = true)
    private String username;

    private String realName;

    // 스프링 시큐리티 규격
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 멤버는 member 권한을 가진다.
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        // username이 admin인 회원은 추가로 admin 권한도 가진다.
        if ("admin".equals(username)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return grantedAuthorities;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}