package me.gimun.demospringmvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* @Configuration +  @EnableWebMvc
* 스프링부트가 기본적으로 제공하는 WEB MVC 설정을 초기화시켜주므로
* 직접 설정 해야함
 */
@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    /*
     * 리소스 핸들러를 추가
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/m/") // 반드시 /로 끝나야함 안그러면 맵핑이 잘 안됨.
                .setCachePeriod(20);
    }
}
