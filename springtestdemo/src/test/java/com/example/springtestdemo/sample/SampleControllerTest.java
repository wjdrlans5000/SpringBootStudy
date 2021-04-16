package com.example.springtestdemo.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
// mocking된 서블릿컨테이너가 구동됨 (webEnvironment환경이 MOCK)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// 랜덤포트를 사용하면 실제로 서블릿(내장톰캣)이 뜸
// 테스트용 restTemplate 또는  test용 webClient를 사용해야한다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// MockMvc 를 자동으로 설정해준다.
// MockMvc 를 Autowired를 통해 주입받을수있음.
//@AutoConfigureMockMvc
public class SampleControllerTest {

//    @Autowired
//    MockMvc mockMvc;

    @Autowired
    TestRestTemplate testRestTemplate;

    /**
     * ApplicationContext에 등록된 빈을 테스트시 MockBean으로 등록한 객체로 교체해준다.
     * 슬라이싱 테스트가 가능함.
     */
//    @MockBean
//    SampleService sampleService;

    /**
     * spring 5에서 webflux부분에 추가된
     * 비동기 테스트 클라이언트
     * 사용하기 위해서는 webflux 관련 의존성이 존재해야한다.
     */
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void hello() throws Exception{
//        mockMvc.perform(get("/hello")) // GET /hello 로 mock 요청을 보낸다.
//                .andExpect(status().isOk()) // 응답코드가 200인지 검증
//                .andExpect(content().string("hello gimun")) // 응답컨텐츠가 hello gimun인지 검증
//                .andDo(print()); // print 를통해 요청과 응답에 대한 정보를 콘솔로 확인가능하다.



        // sampleService의 리턴값을 mocking
//        when(sampleService.getName()).thenReturn("gimun");

        String result = testRestTemplate.getForObject("/hello",String.class);
        assertThat(result).isEqualTo("hello gimun");

        webTestClient.get().uri("/hello").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("hello gimun");
    }
}