package com.example.springbootinit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//ApplicationStartingEvent 에플리케이션 맨처음에 동작하는 이벤트
//컨텍스트 생성이전의 이벤트이므로 빈으로 등록되더라도 콜백X

//ApplicationStartedEvent 에플리케이션이 시작됐을때 동작하는 이벤트
//컨텍스트가 생성된 후 이벤트이므로 빈으로 동록시 콜백O
//@Component
//public class SampleListener implements ApplicationListener<ApplicationStartedEvent> {
//
//    @Override
//    public void onApplicationEvent(ApplicationStartedEvent ApplicationStartedEvent) {
//        System.out.println("====================");
//        System.out.println("Application is started");
//        System.out.println("====================");
//    }
//}

//@Component
//public class SampleListener  {
//
//    public SampleListener(ApplicationArguments arguments){
//        System.out.println("foo : " + arguments.containsOption("foo"));
//        System.out.println("bar : " + arguments.containsOption("bar"));
//    }
//
//}

@Component
@Order(1)
public class SampleListener implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("foo : " + args.containsOption("foo"));
        System.out.println("bar : " + args.containsOption("bar"));
    }

}