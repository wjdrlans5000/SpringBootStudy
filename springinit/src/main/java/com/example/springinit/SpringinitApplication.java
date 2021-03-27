package com.example.springinit;

import org.example.Holoman;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


//@SpringBootConfiguration
//@ComponentScan
//@EnableAutoConfiguration
@SpringBootApplication
public class SpringinitApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SpringinitApplication.class, args);
        SpringApplication application = new SpringApplication(SpringinitApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    /*
    * 컨포넌트 스캔으로 아래 빈을 먼저 등록하고
    * 오토컨피규레이션으로 아래빈을 덮어써버림
    * 따라서 아래 코드가 묻힘.
    * */
    @Bean
    public Holoman holoman(){
        Holoman holoman =  new Holoman();
        holoman.setName("gimun");
        holoman.setHowLong(60);
        return holoman;
    }
}




