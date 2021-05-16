package com.example.springbootsecuritycustomize;

import com.example.springbootsecuritycustomize.account.Account;
import com.example.springbootsecuritycustomize.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountRunner implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account gimun = accountService.creatAccount("gimun","1234");
        System.out.println(gimun.getUsername() + " password : " + gimun.getPassword());
    }
}
