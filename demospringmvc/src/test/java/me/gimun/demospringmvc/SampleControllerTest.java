package me.gimun.demospringmvc;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc; //가짜 서블릿 컨테이너

    @Autowired
    WebClient webClient;

    @Test
    public void hello() throws Exception {
        //요청 "/hellogimun"
        //응답
        // - 모델 name : gimun
        // - 뷰 이름 : hellogimun
/*        mockMvc.perform(get("/hellogimun"))
                .andExpect(status().isOk())
                .andDo(print())  // 렌더링되는 결과를 print -> 타임리프를 사용하여 가능한것임 jsp는 실제 렌더링된 결과를 확인하기 힘듬
                .andExpect(view().name("hellogimun"))
                .andExpect(model().attribute("name","gimun"))
                .andExpect(xpath("//h1").string("gimun"))
                .andExpect(content().string(containsString("gimun"))); // 본문 내용 테스트*/

        // page정보를 가져올때 page interface로 리턴한다.
        // HTMLPage타입을 사용해야지 좀더 다양한 인터페이스를 사용할 수 있다.
        HtmlPage page = webClient.getPage("/hellogimun");
        // h1을 하나 쿼리해서 텍스트를 assertion
        HtmlHeading1 h1 =  page.getFirstByXPath("//h1");
        assertThat(h1.getTextContent()).isEqualToIgnoringCase("gimun");

    }
}