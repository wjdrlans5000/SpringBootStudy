package com.example.springbootsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    //특정 요청이들어올때 특정 뷰로 보내주기만 하면 될 경우 사용.
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry){
//        registry.addViewController("/hello").setViewName("hello");
//    }
}
