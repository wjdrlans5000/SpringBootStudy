package com.example.springbootneo4j;

import com.example.springbootneo4j.account.Account;
import com.example.springbootneo4j.account.AccountRepository;
import com.example.springbootneo4j.account.Role;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Neo4jRunner implements ApplicationRunner {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = new Account();
        account.setUsername("kimun2");
        account.setEmail("rlans2@mail.com");

        Role role = new Role();
        role.setName("user");

        account.getRoles().add(role);

        accountRepository.save(account);

//        Session session =  sessionFactory.openSession();
//        session.save(account);
//        sessionFactory.close();
        System.out.println("finished");
    }
}
