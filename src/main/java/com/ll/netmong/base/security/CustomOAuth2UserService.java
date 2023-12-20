package com.ll.netmong.base.security;

import com.ll.netmong.domain.member.entity.AuthLevel;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.member.entity.ProviderTypeCode;
import com.ll.netmong.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    private String getOAuthId(final OAuth2User oAuth2User, final ProviderTypeCode providerTypeCode) {
        if (providerTypeCode == ProviderTypeCode.NAVER)
            return ((Map<String, String>) oAuth2User.getAttributes().get("response")).get("id");

        return oAuth2User.getName();
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        final ProviderTypeCode providerTypeCode = ProviderTypeCode.codeOf(
                userRequest.getClientRegistration()
                        .getRegistrationId()
                        .toUpperCase()
        );

        final String oauthId = getOAuthId(oAuth2User, providerTypeCode);

        final String username = providerTypeCode + oauthId;

        final String realname = (String) oAuth2User.getAttributes().get("name");

        final String email = (String) oAuth2User.getAttributes().get("email");

        Optional<Member> opMember = memberRepository.findByEmail(email);

        final Member member = opMember.orElseGet(() -> {
            Member build = Member.builder().username(username)
                    .password("")
                    .realName(realname)
                    .email(email)
                    .providerTypeCode(providerTypeCode)
                    .authLevel(AuthLevel.MEMBER)
                    .build();

            return memberRepository.save(build);
        });

        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }
}

class CustomOAuth2User extends User implements OAuth2User {

    public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
