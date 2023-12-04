package com.ll.netmong.member.mock;

import org.springframework.security.config.annotation.ObjectPostProcessor;

public class FakeObjectPostProcessor implements ObjectPostProcessor {
    @Override
    public Object postProcess(Object object) {
        return object;
    }
}
