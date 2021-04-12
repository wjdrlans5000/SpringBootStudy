package com.example.externalsetting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class TestConfiguration {

    @Bean
    public String hello(){
        return "hello";
    }
}
