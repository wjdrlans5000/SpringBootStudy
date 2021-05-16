package com.example.springbootsecuritycustomize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/hello").permitAll() // "/", "/hello" 요청에 대하여는 허용
                .anyRequest().authenticated()   //나머지 요청은 인증필요
                .and()
                .formLogin() //form  로그인을 사용하고
                .and()
                .httpBasic();  //httpbasic을 사용한다.


    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        /*
         * 절대 하면 안되는 방식...
         * 아무런 인코딩과 디코딩을 하지 않게 바꾼것..
         * */
//        return NoOpPasswordEncoder.getInstance();
        /*
        * 이 패스워드 인코더를 사용
        * */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
