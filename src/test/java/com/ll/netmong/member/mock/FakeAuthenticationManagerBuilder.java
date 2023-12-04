package com.ll.netmong.member.mock;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

public class FakeAuthenticationManagerBuilder extends AuthenticationManagerBuilder {
    private static final ObjectPostProcessor<Object> objectPostProcessor = new FakeObjectPostProcessor();

    /**
     * Creates a new instance
     */
    public FakeAuthenticationManagerBuilder() {
        super(objectPostProcessor);
    }
}
