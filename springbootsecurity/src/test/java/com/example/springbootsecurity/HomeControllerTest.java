package com.example.springbootsecurity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class) //WEB 계층관련된 빈만 등록하는 슬라이싱 테스트
//@WithMockUser
public class HomeControllerTest {

    @Autowired
    MockMvc mockMvcv;

    @Test
    @WithMockUser //가짜유저정보를 적용시켜줌
    public void hello() throws Exception {
        mockMvcv.perform(get("/hello")
                .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));

    }

    @Test
    public void hello_without_user() throws Exception {
        mockMvcv.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void my() throws Exception {
        mockMvcv.perform(get("/my"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("my"));
    }
}