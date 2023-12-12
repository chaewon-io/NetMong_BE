package com.ll.netmong.common;

import com.ll.netmong.domain.member.entity.AuthLevel;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.entity.ProviderTypeCode;
import com.ll.netmong.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InitData {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (!memberRepository.existsByUsername("admin")) {
            Member admin = Member.builder()
                    .username("admin")
                    .password("adminadminadmin")
                    .authLevel(AuthLevel.ADMIN)
                    .providerTypeCode(ProviderTypeCode.NETMONG)
                    .realName("NetMong")
                    .email("netmong@netmong.com")
                    .build();

            admin.encryptPassword(passwordEncoder);
            admin.getGrantedAuthorities();
            memberRepository.save(admin);
        }
    }
}
