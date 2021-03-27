package com.example.springinit;

import org.example.Holoman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class HolomanRunner implements ApplicationRunner {

    @Autowired
    Holoman holoman; //autoConfiguration 으로 등록한 빈을 주입받음

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(holoman);
    }
}
