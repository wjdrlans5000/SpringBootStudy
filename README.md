# springBoot-study
- spring boot
  - 운영수준의 애플리케이션을 쉽고 빠르게 개발 가능하도록 기반환경을 제공.
  - 컨벤션한(기본적으로 많이 사용하는) 설정을 제공해준다.
- 스프링 부트 시작하기 Document
  - https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#getting-started-introducing-spring-boot
  - https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#getting-started-maven-installation
  - https://start.spring.io/

# 스프링부트 시작
스프링 부트를 실행하는 메인 클래스 생성
```java
@SpringBootApplication
public class SpringinitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringinitApplication.class, args);
    }

}
```
# 스프링 프로젝트 구조
- 스프링부트에서 추천하는 @SpringBootApplication을 지정하는 메인 에플리케이션 경로
    - 프로젝트에서 사용하는 가장 최상위 패키지(기본패키지)에 두는것을 권장한다.
    - @SpringBootApplication이 지정된 클래스의 패키지부터 하위패키지로 컴포넌트 스캔을 진행한다.
```
 com
 +- example
     +- myapplication
         +- Application.java
         |
         +- customer
         |   +- Customer.java
         |   +- CustomerController.java
         |   +- CustomerService.java
         |   +- CustomerRepository.java
         |
         +- order
             +- Order.java
             +- OrderController.java
             +- OrderService.java
             +- OrderRepository.java
```
# 스프링 부트 원리
- 의존성 관리 이해
  - spring-boot-starter-parent 의 parent는 spring-boot-dependencies
  - spring boot starter를 의존성으로 추가하면 의존성관리를 스트링부트에서 제공하기때문에 우리의 pom에서 의존성관리에 대한 많은부분을 생략할수 있음.
  - parent POM 없이 스프링부트를 사용할경우(별도 의존성관리가 필요해서) dependencyManagement 엘리먼트를 사용하여 spring-boot-dependencies 직접 넣어준다.(parent에 자바컴파일버전이나 인코딩 설정, 리소스필터링, 의존성관리등 스프링부트에 최적화된 설정을 제공하기 때문에 별도 설정이 필요하므로 parent 사용을 추천)
  - https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#using-boot-structuring-your-code
  - 별도 버전 명시 안해줘도 됨.
  - https://mvnrepository.com/
  - 스프링부트가 의존성을 관리해주지않는 경우에는 버전을 명시해주는게 좋음.
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
```
- - 스프링부트가 관리하는 의존성의 버전을 바꾸고싶은경우에는 parent의 parent인 spring-boot-dependencies에 존재하는 프로퍼티 엘리먼트를 오버라이딩 해주면된다.
```xml
<properties>
    <spring.version>5.0.7.RELEASE</spring.version>
</properties>
```
- 자동 설정 이해
  - @SpringBootApplication 안에 @SpringBootConfiguration/@ComponentScan/@EnableAutoConfiguration 가 있음
  - 빈은 사실 두 단계로 나눠서 읽힘
    - 1단계 : @ComponentScan
    - 2단계 : @EnableAutoConfiguration
    - 따라서 @EnableAutoConfiguration 없어도 컴포넌트 스캔 문제 없음.
  - @ComponentScan
    - @Component 애노테이션으로 등록된 객체들을 빈으로 등록 
    - @Configuration @Repository @Service @Controller @RestController 등 또한 빈으로 등록
  - @EnableAutoConfiguration
    - spring.factories 
      - org.springframework.boot.autoconfigure 하위에 META-INF 아래에 위치. 
      - spring.factories 내에 org.springframework.boot.autoconfigure.EnableAutoConfiguration 키값 밑에 설정된 클래스들이 autoconfiguration 대상이 됨
      - org.springframework.boot.autoconfigure 하위 web > servlet > WebMvcAutoConfiguration.class 에 설정된 Conditional(조건)에 따라서 특정 빈을 등록하거나 등록하지 않음
    - @Configuration
    - @ConditionalOnXxxYyyZzz
```java
@Configuration(
    proxyBeanMethods = false
)
@ConditionalOnWebApplication(
    type = Type.SERVLET
)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
@AutoConfigureOrder(-2147483638)
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class})
public class WebMvcAutoConfiguration {
```
- spring boot WebApplication이 아닌 애플리케이션으로 실행
```java
        SpringApplication application = new SpringApplication(SpringinitApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
```
- 자동설정 만들기
  - Xxx-Spring-Boot-Autoconfigure 모듈: 자동 설정
  - Xxx-Spring-Boot-Starter 모듈: 필요한 의존성 정의
  - 그냥 하나로 만들고 싶을 때는?
    - Xxx-Spring-Boot-Starter
  - 구현방법
    - 1. 메이븐으로 프로젝트 생성
    - 2. 의존성 추가
```xml
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-autoconfigure</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-autoconfigure-processor</artifactId>
      <optional>true</optional>
    </dependency>
  </dependencies>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>2.0.3.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```
-  -
      - 3. @Configuration 파일작성
      - 4. src/main/resource/METEA-INF에 spring.factories 파일 만들기
      - 5. spring.factories 안에 자동 설정 파일 추가
        - org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
        - FQCN,\
        - FQCN
      - 6. mvn install
   - 덮어쓰기 방지하기
    - @ConditionalOnMissingBean
