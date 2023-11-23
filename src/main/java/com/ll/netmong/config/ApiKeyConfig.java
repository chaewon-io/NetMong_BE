package com.ll.netmong.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfig {

    @Value("${api.parkApiKey}")
    private String parkApiKey;

    @Bean
    public ApiKeys apiKeys() {
        return new ApiKeys(parkApiKey);
    }

}