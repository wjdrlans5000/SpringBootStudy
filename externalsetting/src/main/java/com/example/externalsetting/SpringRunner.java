package com.example.externalsetting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SpringRunner  implements ApplicationRunner {

//    @Value("${gimun.fullName}")
//    private String name;
//
//    @Value("${gimun.age}")
//    private int age;

    @Autowired
    private String hello;

    //ConfigurationProperties 활용하여 프로퍼티 설정
    @Autowired
    private GimunProperties gimunProperties;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("======================");
        System.out.println(hello);
        System.out.println(gimunProperties.getFullName());
        System.out.println(gimunProperties.getAge());
        System.out.println(gimunProperties.getSessionTimount());
        System.out.println("======================");

    }
}
