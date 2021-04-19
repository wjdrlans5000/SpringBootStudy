package me.gimun.demospringmvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* @Configuration +  @EnableWebMvc
* 스프링부트가 기본적으로 제공하는 WEB MVC 설정을 초기화시켜주므로
* 직접 설정 해야함
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

}
