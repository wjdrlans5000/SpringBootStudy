package com.example.externalsetting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class BaseConfiguration {

    @Bean
    public String hello(){
        return "helloTest";
    }
}
