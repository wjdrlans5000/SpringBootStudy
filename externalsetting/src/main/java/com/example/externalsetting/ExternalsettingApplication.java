package com.example.externalsetting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GimunProperties.class)
public class ExternalsettingApplication {

    public static void main(String[] args) {
//        new SpringApplicationBuilder()
//                .sources(ExternalsettingApplication.class)
//                .run(args);
//    }
        SpringApplication application = new SpringApplication(ExternalsettingApplication.class);
//        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

}
