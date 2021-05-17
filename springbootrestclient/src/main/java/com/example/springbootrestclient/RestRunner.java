package com.example.springbootrestclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestRunner  implements ApplicationRunner {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    WebClient.Builder builder;

//    WebClient webClient;
//
//    public RestRunner(WebClient.Builder builder){
//        this.webClient = builder.build();
//    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        WebClient webClient = builder
                .baseUrl("http://localhost::8080")
                .build();

//        RestTemplate restTemplate =  restTemplateBuilder.build();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        /*
          블럭킹 I/O 기반 이기때문에 해당 요청 라인의 응답이 오기까지 다음라인으로 진행되지않음. (동기적)
        * */
        // TODO /hello
//        String helloResult = restTemplate.getForObject("http://localhost:8080/hello",String.class);
//        System.out.println(helloResult);
        // TODO /world
//        String worldResult = restTemplate.getForObject("http://localhost:8080/world",String.class);
//        System.out.println(worldResult);



        /* Stream API 인 Mono임 subscribe 하기전까진 진행되지않는다. */
        /* subscribe 해야지만 해당 요청을 실행함. Non-Blocking*/
        Mono<String> helloMono = webClient.get().uri("/hello")
                    .retrieve()
                    .bodyToMono(String.class);
        helloMono.subscribe(s -> {
            System.out.println(s);
            if(stopWatch.isRunning()){
                stopWatch.stop();
            }
            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
        });

        Mono<String> worldMono  = webClient.get().uri("/world")
                    .retrieve()
                    .bodyToMono(String.class);
        worldMono.subscribe(s -> {
            System.out.println(s);
            if(stopWatch.isRunning()){
                stopWatch.stop();
            }
            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
        });

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
