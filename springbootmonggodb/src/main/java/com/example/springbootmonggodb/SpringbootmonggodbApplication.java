package com.example.springbootmonggodb;

import com.example.springbootmonggodb.account.Account;
import com.example.springbootmonggodb.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class SpringbootmonggodbApplication {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AccountRepository accountRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootmonggodbApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return args -> {
            Account account = new Account();
            account.setEmail("wjdrlans4000@naver.com");
            account.setUsername("gimun");

//            mongoTemplate.insert(account);
            accountRepository.insert(account);
            System.out.println("finished");
        };
    }

}
