package com.ll.netmong.member.mock;

import com.ll.netmong.base.jwt.TokenDto;
import com.ll.netmong.base.jwt.TokenProvider;
import com.ll.netmong.domain.member.dto.LoginDto;

public class FakeTokenProvider extends TokenProvider {
    public FakeTokenProvider(String secret, long tokenValidityInSeconds, String secret2, long tokenValidityInSeconds2) {
        super(secret, tokenValidityInSeconds, secret2, tokenValidityInSeconds2);
    }

    public TokenDto provideTokenWithLoginDto(LoginDto loginDto){

        return new TokenDto("ACCESS_TOKEN","REFRESH_TOKEN");
    }

}
