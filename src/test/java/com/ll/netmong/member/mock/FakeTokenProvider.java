package com.ll.netmong.member.mock;

import com.ll.netmong.jwt.TokenProvider;

public class FakeTokenProvider extends TokenProvider {
    public FakeTokenProvider(String secret, long tokenValidityInSeconds, String secret2, long tokenValidityInSeconds2) {
        super(secret, tokenValidityInSeconds, secret2, tokenValidityInSeconds2);
    }
}
