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
   - 빈 재정의 수고 덜기
     - @ConfigurationProperties(“holoman”)
     - @EnableConfigurationProperties(HolomanProperties.class)
     - 프로퍼티 키값 자동 완성
```java
@Configuration
@EnableConfigurationProperties(HolomanProperties.class)
public class HolomanConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Holoman holoman(HolomanProperties properties){
        Holoman holoman = new Holoman();
        holoman.setHowLong(properties.getHowLong());
        holoman.setName(properties.getName());
        return holoman;
    }
}
```
```java
@ConfigurationProperties("holoman")
public class HolomanProperties {
    private  String name;

    private int howLong;

    public int getHowLong() {
        return howLong;
    }

    public void setHowLong(int howLong) {
        this.howLong = howLong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
```properties
application.properties
holoman.name = lazy
holoman.howLong = 600
```
# 내장 웹 서버 이해
  - 스프링 부트에 톰캣이 임베디드 되어있음
    - 톰캣 객체 생성
    - 포트설정
    - 톰캣에 컨텍스트 추가
    - 서블릿 만들기
    - 톰캣에 서블릿 추가
    - 컨텍스트에 서블릿 맵핑
    - 톰캣 실행 및 대기
  - 이 모든 과정을 보다 상세히 또 유연하게 설정하고 실행해주는게 바로 스프링 부트의 자동 설정.
    - ServletWebServerFactoryAutoConfiguration (서블릿 웹 서버 생성)
      - TomcatServletWebServerFactoryCustomizer (서버 커스터마이징)
    - DispatcherServletAutoConfiguration
      - 서블릿 만들고 등록
      
# 내장 웹 서버 응용 1부 : 컨테이너와 서버 포트
- Spring boot 는 기본적으로 Tomcat을 내장서버로 자동 설정된다.
- 다른 서블릿 컨테이너로 변경가능
  - 1. spring-boot-starter-web에서 tomcat을 exclusions 설정
  - 2. jetty , undertow 등 사용하고싶은 컨테이너를 의존성으로 추가하면 해당 웹서버로 구동할 수 있다.
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-undertow</artifactId>
        </dependency>
```
- 의존성에 웹서버가 추가되어있으면 스프링 부트는 기본적으로 웹 어플리케이션으로 실행한다.
- properties 파일에 추가하여 포트 등 웹서버 설정을 변경할수 있음.
```xml
#스프링 부트 애플리케이션 타입이 웹서버가 아니게 설정
spring.main.web-application-type=none
#웹서버 포트변경
server.port=7070
#랜덤포트사용
server.port=0
```
- 스프링 부트 웹서버가 초기화되었을때 콜백되는 이벤트 리스너 설정
- 스프링 부트에서 권장하는 베스트 프렉티스.

```java
@Component
//ServletWebServerInitializedEvent < WebServer가 초기화가되면 해당 이벤트에대한 콜백이 이루어짐
public class PortListener implements ApplicationListener<ServletWebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent servletWebServerInitializedEvent) {
      //웹서버 포트 가져오기
        ServletWebServerApplicationContext applicationContext = servletWebServerInitializedEvent.getApplicationContext();
        System.out.println(applicationContext.getWebServer().getPort());
    }
}
```

# 내장 웹 서버 응용 2부 : HTTPS와 HTTP2 적용
- https 적용 - SSL인증서를 생성한다
- window에서 keysotre명령어는 jre 설치 경로에서 진행해야함.
- 터미널을 관리자권한으로 실행해야함.
```
keytool -genkey -alias spring -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 4000
```
- 인증서 정보를 application.properties 파일에 추가
```file
server.ssl.key-store=keystore.p12
server.ssl.key-store-type=PKCS12
server.ssl.key-store-password=123456
server.ssl.key-alias=spring
```
- 해당 설정 후 웹 서버 실행시 https아니면 접근할수 없게됨.(스프링 부트는 http 커넥터가 하나이며 그 커넥터에 https를 적용했기에 http를 받을 커넥터가 없음)
- 아래 curl 명령어로 접근해보면 접근이 되지않고 안내문구가 나오게 된다.
```
curl -I --http2 https://localhost:8080/hello
```
- 그이유는 SSL인증서를 로컬에서 생성했기때문에 해당 인증서가 공인인증서가 아니기때문에(pubKey정보를 모르기때문)
- -k 옵션을줘서 무시하면 200 코드와 함께 접근이 가능해진다.
```
curl -I -k --http2 https://localhost:8080/hello
```
- http를 추가로 받기위해선 커넥터를 추가해주어야 함
- 다음과 같이 Bean으로 TomcatServletWebServerFactory로 커넥터를 생성하여 등록해주고나면 https 와 http 요청 모두 받을 수 있다.
- port는 다르게 설정필요
```java
    @Bean
    public ServletWebServerFactory servletWebServerFactory () {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8090);
        return connector;
    }
```
- http2를 활성화하는 방법
  - application.properties에 다음 추가
```xml
# http2를 지원하는 설정 (undertow는 https설정이 되어있다면 추가적인 설정이 필요가없다.)
# tomcat9,jdk8 이하 버전에서는 매우 복잡. 아래 문서 참고 tomcat9, jdk9 이상 사용 권장(꼭!)
# https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-embedded-web-servers
server.http2.enabled=true
```

# 독립적으로 실행 가능한 jars
- spring boot maven plugin 에 관련된 이야기
  - mvn clean package 명령어를 실행하면 , target폴더 아래 파일들을 삭제하고(clean) 패키징(package)하여 실행가능한 JAR파일이 생성된다.
  - 해당 jar파일 하나로 앱이 구동된다.
  - 앱에 필요한 의존성 들도 같이 jar파일 하나에 같이 들어간다.
  - 과거 “uber” jar 를 사용
    - 모든 클래스 (의존성 및 애플리케이션)를 하나로 압축하는 방법
    - 뭐가 어디에서 온건지 알 수가 없음
      - 무슨 라이브러리를 쓰는건지..
  - 스프링 부트의 전략
    - 내장 JAR : 기본적으로 자바에는 내장 JAR를 로딩하는 표준적인 방법이 없음.
    - 애플리케이션 클래스와 라이브러리 위치 구분
    - org.springframework.boot.loader.jar.JarFile을 사용해서 내장 JAR를 읽는다.
  - https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-executable-jar-format.html
  - mvn package 명령 실행시 spring-boot-starter-parent 2.4.4 버전에서 spring-boot-maven-plugin 버전관련? 오류 발생하네...
    - spring-boot-starter-parent 2.4.4 의 parent인 spring-boot-dependencies의 프로퍼티 maven-resources-plugin-version이 3.2.0 인데 오류발생
  - 해결방법..
    - 1. 2.3.4.RELEASE에서 정상동작 (2.4.0까지 안됨 릴리즈버전에서 되는듯..)
    - 2. spring-boot-dependencies의 프로퍼티 maven-resources-plugin-version 을 하위버전으로 오버라이딩
    ```xml
        <properties>
          <maven-resources-plugin.version>3.1.0</maven-resources-plugin.version>
        </properties>
    ```

# Spring boot 원리 정리
- 의존성 관리
  - spring-boot-starter 는 스프링부트의 의존성을 관리
  - spring-boot-starter-parent 가 스프링 부트 의존성 관리의 핵심
    - 스프링부트가 관리하는 주요라이브러리의 버전들을 볼수 있음(spring-boot-dependencies에서)
  - parent로 받는방법과 denpendencyManagement로 받는것과는 큰 차이가 있음(절대 같은게 아님)
- 자동 설정
  - 스프링부트는 빈을 두단계에 거쳐서 등록한다.
  - 1. @ComponentScan
  - 2. @EnableAutoConfiguration
  - 위 두 단계로 스캔을 하며 AutoConfiguration 클래스 목록(@ConditionalOnXxxYyyZzz(컨디셔널 온 미싱빈 등))을 참조하여 자동설정을 시작 
- 내장 웹서버
  - 스탠다드얼론(독립적으로 실행가능한) 웹 애플리케이션을 제공함.
  - 스프링부트는 웹서버가 아니다.

# Spring boot 활용 - springApplication
- 기본 로그 레벨은 INFO
- 다음은 기본 스프링부트 애플리케이션이다. 아래처럼 static메서드를 활용하여 스프링부트 애플리케이션을 실행할경우 SpringApplication 클래스가 제공하는 다양한 커스터마이징을 활용하기가 어렵기때문에 SpringApplication 클래스의 인스턴스를 생성하여 실행 하는게 좋다.
```java
@SpringBootApplication
public class Applicaiton {

    public static void main(String[] args){
        SpringApplication.run(Applicaiton.class,args);
        //인스턴스 생성하여 실행 하는게 좋음
        //SpringApplication application = new SpringApplication();
        //application.run(args);
    }
}
```
- spring boot application 실행시 ,인텔리제이 vm옵션으로 -Ddebug 또는 program argument로 --debug옵션을 주게되면 디버그모드로 애플리케이션이 동작을한다.
  - 로그레벨도 Debug레벨도 동작한다.
- 애플리케이션 실행시 출력되는 Debug 로그는 스프링부트가 제공하는 자동설정중 어떠한 설정이 자동설정되었는지, 혹은 어떤 자동설정이 되지않았는지를 알려준다.
- Spring boot FailureAnalyers
  - FailureAnalyers는 스프링 애플리케이션 실행중 에러가 발생했을때 해당 에러메시지를 좀더 깔끔하고 보기 쉽게 출력하도록 도와준다.
  - 스프링부트는 기본적으로 몇몇 FailureAnalyers가 등록되어있으며 직접 등록할수도있다.
- Spring boot banner
  - 애플리케이션 실행시 보이는 배너를 커스터마이징 할 수있다.
  - src > main > resources > banner.txt || gif || png || jpg 배너파일을 위치시키면 스프링부트 애플리케이션 실행시 해당 배너가 출력된다.
  - 스프링의 버전 등을 출력할 수 있는 변수들을 제공한다.
  - 일부 변수는 MANIFEST파일이 생성되어야 출력가능하다.(ex)${pplication.version})
  - 패키징시 메니페스트 파일이 만들어지므로 jar파일로 패키징하여 실행하면 위 정보가 출력됨.
  - 배너를 끄고싶은경우 애플리케이션 실행 옵션을 줄 수 있다.
  ```java
    @SpringBootApplication
    public class Applicaiton {

        public static void main(String[] args){
            SpringApplication application = new SpringApplication();
             //배너를 끄기
            application.setBannerMode(Banner.Mode.OFF);
            application.run(args);
        }
    }
  ```
- failure/banner 참조 https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-spring-application
- SpringBootApplication 실행방법으로 SpringApplicationBuilder를 활용하여 빌더패턴을 사용하는 방법도 있다.
```java
  @SpringBootApplication
  public class Applicaiton {

      public static void main(String[] args){
          new SpringApplicationBuilder()
                      .sources(Applicaiton.class)
                      .run(args);
      }
  }
```
- spring 에서 제공해주는 ApplicationEvent
  - 이벤트의 다양한 시점이 존재한다.(애플리케이션 구동 전후, 준비완료, 실패 등)
  - 애플리케이션 리스너의 이벤트 발생시점이 중요하다.
  - 애플리케이션 컨텍스트가 생성 된 후 이벤트일 경우
    - 리스너를 생성한뒤 빈으로 등록되어있다면 , 해당 이벤트가 발생시 이벤트리스너가 콜백된다.
  - 애플리케이션 컨텍스트가 생성되기 이전의 이벤트일 경우
    - 빈으로 등록하고 해당 이벤트가 발생하여도 리스너가 동작하지않는다.
    - 이러한 경우에는 직접 등록을 해주어야한다.
  ```java
      /**
    * 애플리케이션 리스너 등록 
    * " 애플리케이션 이벤트 발생시점에 주의 " 
    */
    //ApplicationStartingEvent 에플리케이션 맨처음에 동작하는 이벤트
    //컨텍스트 생성이전의 이벤트이므로 빈으로 등록되더라도 콜백X
    //@Component
    public class SimpleListener implements ApplicationListener<ApplicationStartingEvent> {

        @Override
        public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
          System.out.println("====================");
          System.out.println("Application is starting");
          System.out.println("====================");
        }
    }
    @SpringBootApplication
    public class Applicaiton {

        public static void main(String[] args){
            SpringApplication application = new SpringApplication();
            //애플리케이션 컨텍스트 생성이전의 이벤트일 경우 수동 등록
            application.addListeners(new SimpleListener());
            application.run(args);
        }
    }
  ```
- WebApplicationType 설정
- 스프링부트 애플리케이션의 타입은 NONE, SERVLET , REACTIVE 로 크게 3가지로 분류
  - 기본적으로 spring-web-mvc가 존재한다면 SERVLET으로 실행
  - spring webflux가 존재한다면 REACTIVE로 실행한다.(servlet이 없을경우)
  - 둘다 없으면 NONE로 실행
- 애플리케이션 아규먼트 사용하기
  - 애플리케이션 실행시 --옵션 으로 들어오는 경우 (Configurations의 program arguments)
  - jar -000.jar --bar 이런식으로 아규먼트를 줌 jar파일 실행시
- 애플리케이션 실행한 뒤 무언가를 실행하고 싶은경우
  - ApplicationRunner (추천) 또는 CommandLineRunner(jvm옵션은 무시)
    - ApplicationArguments를 인자로 받는다.  (추상화된 API를 사용하여 코딩이 가능함) 
    ```java
      @Component
      public class SampleListener implements ApplicationRunner {

          @Override
          public void run(ApplicationArguments args) throws Exception {
              System.out.println("foo : " + args.containsOption("foo"));
              System.out.println("bar : " + args.containsOption("bar"));
          }

      }
    ```
  - 순서 지정 가능 @Order(숫자가 낮을수록 우선순위)

# Spring boot 활용 - 외부설정
- 사용할 수 있는 외부 설정
  - properties
  - YAML
  - 환경 변수
  - 커맨드 라인 아규먼트
- 스프링부트에서 properties를 사용한 외부설정의 경우 application.properties파일을 이용하는것이 일반적이다.
- 사용방법
  - application.properties파일에 사용할 속성 정의
  ```
   #application.properties 파일설정
   gimun.name = gimun
  ```
  - org.springframework.beans.factory.annotation.Value 애노테이션을 활용
  ```java
      @Component
    public class SpringRunner  implements ApplicationRunner {

        @Value("{gimun.name}")
        private String name;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("======================");
            System.out.println(name);
            System.out.println("======================");

        }
    }
  ```
- 프로퍼티 우선 순위
  - 1. 유저 홈 디렉토리에 있는 spring-boot-dev-tools.properties
  - 2. 테스트에 있는 @TestPropertySource
  - 3. @SpringBootTest 애노테이션의 properties 애트리뷰트
  - 4. 커맨드 라인 아규먼트
  - 5. SPRING_APPLICATION_JSON (환경 변수 또는 시스템 프로티) 에 들어있는 프로퍼티
  - 6. ServletConfig 파라미터
  - 7. ServletContext 파라미터
  - 8. java:comp/env JNDI 애트리뷰트
  - 9. System.getProperties() 자바 시스템 프로퍼티
  - 10. OS 환경 변수
  - 11. RandomValuePropertySource
  - 12. JAR 밖에 있는 특정 프로파일용 application properties
  - 13. JAR 안에 있는 특정 프로파일용 application properties
  - 14. JAR 밖에 있는 application properties
  - 15. JAR 안에 있는 application properties
  - 16. @PropertySource
  - 17. 기본 프로퍼티 (SpringApplication.setDefaultProperties)
- application.properties 우선 순위 (높은게 낮은걸 덮어 씁니다.)
