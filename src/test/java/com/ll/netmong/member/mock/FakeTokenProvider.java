package com.ll.netmong.member.mock;

import com.ll.netmong.base.jwt.TokenDto;
import com.ll.netmong.base.jwt.TokenProvider;
import org.springframework.security.core.Authentication;

public class FakeTokenProvider extends TokenProvider {
    public FakeTokenProvider(String secret, long tokenValidityInSeconds, String secret2, long tokenValidityInSeconds2) {
        super(secret, tokenValidityInSeconds, secret2, tokenValidityInSeconds2);
    }

    @Override
    public TokenDto createToken(Authentication authentication) {

        return new TokenDto("ACCESS_TOKEN","REFRESH_TOKEN");
    }
}
