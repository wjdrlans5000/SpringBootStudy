package com.example.springbootinit;


import org.example.Holoman;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@SpringBootConfiguration
//@ComponentScan
//@EnableAutoConfiguration
@SpringBootApplication
public class SpringBootInitApplication {

//    public static void main(String[] args) throws LifecycleException {
//
//        // 톰캣 객체생성
//        Tomcat tomcat = new Tomcat();
//        // 톰캣 포트설정
//        tomcat.setPort(8080);
//        // 컨텍스트 설정
//        Context context = tomcat.addContext("/", "/");
//
//        HttpServlet servlet = new HttpServlet() {
//            @Override
//            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                PrintWriter writer = resp.getWriter();
//                writer.println("<html><head><title>HelloTomcat</title></head>");
//                writer.println("<body>Hello world</body></html>");
//            }
//        };
//
//        // 톰캣에 서블릿 추가
//        tomcat.addServlet("/","hello",servlet);
//        // 컨텍스트에 서블릿매핑
//        context.addServletMappingDecoded("/hello","hello");
//
//        // 톰캣실행
//        tomcat.start();
//
//        tomcat.getServer().await();

//    }

    public static void main(String[] args)  {
        SpringApplication application = new SpringApplication(SpringBootInitApplication.class);
//        application.setWebApplicationType(WebApplicationType.NONE);
//        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run(args);
    }
    /*
     * 컨포넌트 스캔으로 아래 빈을 먼저 등록하고
     * 오토컨피규레이션으로 아래빈을 덮어써버림
     * 따라서 아래 코드가 묻힘.
     * @ConditionalOnMissingBean 설정 :  중복설정시 덮어쓰기 방지
     * */
//    @Bean
//    public Holoman holoman(){
//        Holoman holoman =  new Holoman();
//        holoman.setName("gimun");
//        holoman.setHowLong(60);
//        return holoman;
//    }

}
