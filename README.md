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
- https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config
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
  > application.properties 우선 순위 (높은게 낮은걸 덮어 씁니다.)

- 모든 Property들은 Environment 객체를 활용하여 사용할 수 있다.
- test 실행시 properties 발생현상
  - src 하위를 빌드한다.
  - test 하위를 빌드한다.
  - test/resources/application.properties가 존재한다면 , test/하위에 존재하는 properties로 src의 프로퍼티가 오버라이딩 된다.
- test용 properties를 사용할때 주의할점
  - 빌드시 src 디렉터리를 먼저 빌드하고 test 디렉터리를 빌드하는데 src 에 존재하는 properties에는 존재하지만 , test의 properties에는 존재하지않는 프로퍼티가 있고 , 그 값을 참조하는 경우 예     외가 발생한다. 즉, 잠정적 버그를 발생시킬 여지가 있음.
- properties 파일 내에서 Random값을 사용하는 방법
  - ${random} 사용
  - server.port 에는 random을 사용하지 말아야 하는 이유 ?
  - server.port=0으로 랜덤값 지정(포트번호를 가용가능한 범위 내에서 랜덤값을 부여, random 변수는 그것을 고려하지않은 랜덤값을 부여한다.)
  ```
    gimun.age=${random.int}
    #port랜덤 지정
    server.port=0
  ```

- SpringBootTest의 propertie 애트리뷰트를 활용한 방법
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExternalsettingApplicationTests {

    @Autowired
    Environment environment;

    @Test
    public void contextLoads() {
        assertThat(environment.getProperty("gimun.name")).isEqualTo("gimuntest");
    }

}
```

- @TestPropertySource 애노테이션을 활용하는 방법
- properties 애트리뷰트로 직접 오버라이딩하거나 ,location으로 properties파일을 명시할 수 있다.
```java
@RunWith(SpringRunner.class)
//@TestPropertySource(properties = {"gimun.name=gimuntest","gimun.name=gimuntest"})
@TestPropertySource(locations = "classpath:/test.properties")
@SpringBootTest()
public class ExternalsettingApplicationTests {

    @Autowired
    Environment environment;

    @Test
    public void contextLoads() {
        assertThat(environment.getProperty("gimun.name")).isEqualTo("gimuntest");
    }

}
```
- application.properties 자체의 우선순위
  - projectRoot/config 하위
  - projectRoot
  - classpath:/config 하위
  - classpath:하위
  > 위에가 우선순위가 높다.

- 타입-세이프 프로퍼티 @ConfigurationProperties
  - 여러 프로퍼티를 묶어서 읽어올 수 있음
  - java bean spec을 따라서 프로퍼티값들을  바인딩을 해주기때문에 getter setter가 필요함.
  - 빈으로 등록해서 다른 빈에 주입할 수 있음 (@Component, @Bean)
  - @EnableConfigurationProperties
    - @ConfigurationProperties 를 사용하는 클래스들을 활성화
    - 이미 활성화 애노테이션으로 등록되어있기 때문에 해당 클래스들을 빈으로 등록해주기만 하면된다.
    ```java
      // ConfigurationProperties 애노테이션에 알림이 뜨는경우
      // 해당 메타정보를 기반으로 자동완성을 제공해주는 플러그인을 추가하라는 알림이므로 pom.xml에 해당 디펜던시 추가
      @Component
      @ConfigurationProperties("gimun")
      public class GimunProperties {
          String name;

          int age;

          String fullName;

          public String getName() {
              return name;
          }

          public void setName(String name) {
              this.name = name;
          }

          public int getAge() {
              return age;
          }

          public void setAge(int age) {
              this.age = age;
          }

          public String getFullName() {
              return fullName;
          }

          public void setFullName(String fullName) {
              this.fullName = fullName;
          }
      }

    ```
- 융통성 있는 바인딩(Relexed Binding)
  - amel-case로 작성하지않고 , kebab-case(-) or underscore-case(_)로 작성하여도 바인딩을 해준다.
- Type-Conversion 지원
  - 스프링 프레임워크가 지원하는 컨버전 서비스를 통해서 타입 컨버전이 일어남.
- @Duration Unit (스프링부트가 지원하는 컨버전)
  - 특정 시간단위 로 받고싶을경우 바인딩을 지원한다.
```java
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration sessionTimount = Duration.ofSeconds(30);
```
- @Duration 애노테이션을 사용하지않아도 properties에 값을 할당할때 s,ms 등 suffix를 통해 Duration으로 바인딩 할수 있도록 지원한다.
```
  gimun.name = gimun
  gimun.age = ${random.int(1,100}
  gimun.fullName = ${gimun.name} jeong
  gimun.sessionTimeout = 25s
```
```java
@Component
@ConfigurationProperties("gimun")
public class JuneYoungProperties {

    private String name;

    private int age;

    private String fullName;

//    @DurationUnit(ChronoUnit.SECONDS)
    private Duration sessionTimount = Duration.ofSeconds(30);

    public Duration getSessionTimount() {
        return sessionTimount;
    }

    public void setSecound(Duration sessionTimount) {
        this.sessionTimount = sessionTimount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
```
- 프로퍼티 값 검증
  - 프로퍼티값이 바인딩 될때 검증이 가능하다.
  - 상단에 @Validated 애노테이션을 선언하고, 검증하고싶은 필드상단에 JSR-303 애노테이션(ex.@NotEmpty) (구현체는 hibernate-validator) 를 활용하여 검증을 할 수 있다.
  - FailureAnaylizer 가 에러메시지를 보기좋게 포매팅하여 보여준다.
```java
@Component
@ConfigurationProperties("gimun")
@Validated
public class GimunProperties {

    @NotEmpty
    private String name;

    private int age;

    private String fullName;

//    @DurationUnit(ChronoUnit.SECONDS)
    private Duration sessionTimount = Duration.ofSeconds(30);

    public Duration getSessionTimount() {
        return sessionTimount;
    }

    public void setSessionTimount(Duration sessionTimount) {
        this.sessionTimount = sessionTimount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

```
- @Value
  - SpEL 을 사용할 수 있음.
  - 아주 정확히 써야함..

# 스프링 부트 활용 - 프로파일
- @Profile 애노테이션 
  - 스프링 프레임워크에서 지원해주는 기능 
  - 특정 프로파일에서만(ex.prod) 사용가능
  - @Configuration, @Component
- 각 프로파일별로 설정파일 작성
```java
@Profile("prod")
@Configuration
public class TestConfiguration {

    @Bean
    public String hello(){
        return "hello";
    }
}

@Profile("test")
@Configuration
public class BaseConfiguration {

    @Bean
    public String hello(){
        return "helloTest";
    }
}

```
- application.properties 파일에 활성화할 프로파일을 정의
```
# 프로파일 활성화
spring.profiles.active = test
```
- profiles도 properties의 우선순위에 영향을 받는다.
- java -jar 옵션으로 실행시 커맨드라인 아규먼트가 우선순위가 더 높으므로 실행시 profiles을 지정하여 실행이 가능함.
- IDE의 파라미터 아규먼트에 --spring.profiles.active=prod 설정하여 개발환경에서 profiles 지정하여 실행가능.
```
// maven package 
mvn clean package

// prod 프로파일로 애플리케이션 실행
jar -jar example.jar --spring.profiles.active=prod  
```
- profile용 properties도 생성이 가능하다.
  - application-{profile}.properties 의 명으로 작성이 가능하다.
  - profile용 properties파일이 기본 application.properties파일보다 우선순위가 높기때문에 해당하는 profile의 properties로 오버라이딩된다.
- properties 내에서 특정 프로파일 설정을 포함시키는 방법(인클루드)
  - spring.profiles.include=프로파일명
  ```
  gimun.name = gimun prod
  # 설정이 읽혀졌을때 추가할 프로파일
  spring.profiles.include= = proddb
  ```
 
# Spring boot 활용 - 로깅
- 스프링부트는 Commons Logging을 사용한다
- 스프링 코어에서 Commons Logging 에서 사용하기때문에 사용..(스프링코어가 만들어질 당시 SLF4J가 없었음)
- SLF4J를 사용하려면 의존성 설정을 잘해주어야함 ...
- 스프링5 로거관련 변경사항 : https://docs.spring.io/spring/docs/5.0.0.RC3/spring-framework-reference/overview.html#overview-logging

### 로깅 퍼사드 vs 로거
- Commons Logging, SLF4j(로깅퍼사드)
  - 실제 로깅을 하는 구현체가아니라 추상화 해놓은 API
  - 프레임워크들은 로깅 퍼사드를 이용하여 개발함.
  - 로깅퍼사드의 장점은 로깅퍼사드 밑에 로거(실제 구현체)들을 교체 할 수 있음(자신들이 원하는 로거를 쓸수 있도록)
  - JUL, LOG4J2, LogBack (로거) 등으로 원하는 실제 구현체로 교체하여 사용
- Spring-JCL(Jakarta Common Logging) (스프링 5에서 만든 모듈로 컴파일시점에 SLF4J나 LOG4J2로 변경할수 있는 인터페이스)
  - Commons Logging > SLF4j 로 변경할수 있도록 제공(Commons Logging이 문제가 많아 구조적으로 더 심플하고 안전한 SLF4j 라이브러리가 만들어짐)
  - pom.xml에 exclusion 안해도 됨.(스프링부트 1에서는 SLF4J를 사용하려면 Commons Logging을 exclusion 해야했음) 
 
### 정리
- Commons Logging > SLF4j > LogBack
- 의존성을 보면 spring-boot-starter-web > spring-boot-starter > spring-boot-starte-logging 을 살펴본다.
  - 1. jul-to-slf4j(jul(자바유틸로깅)을 쓰는것은 slf4j로 보내라)
  - 2. log4j-to-slf4j(log4j를 쓰는코드도 slf4j로)
  - 3. slf4j-api (slf4j로 받고)
  - 4. logback-core (로그백으로 로그를 남김)
- 스프링부트는 최종적으로 LogBack(로거) 을 사용

### 스프링 부트 로깅
- 기본포맷
  - [날짜] 로깅레벨 PID Thread class..
- --debug (일부 핵심라이브러리만 디버깅모드)
- --trace (모든 로깅을 디버그모드로 ..)
- 컬러출력: spring.output.ansi.enabled
- 파일출력: logging.file(로그파일) logging.path(디렉터리) 설정
  - 10mb마다 아카이빙됨(설정가능)
- 로그레벨: logging.level.packagename=LOGGING_LEVEL (패키지별로 로깅레벨 설정)

### 커스텀 로그파일
- https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html
- Logback: logback.xml || logback-spring.xml(추천! 스프링부트에서 지원하는 extension을 사용가능함.)
- Log4j2: log4j2-spring.xml
- JUL(비추): logging.properties
- Logback extension
  - 프로파일 특정 프로파일별로 설정이 가능함.
  - Environment 프로퍼티

### 로거를 Log4j2로 변경하기
- https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html#howto-configure-log4j-for-logging
- spring-boot-starter-web 에 포함된 spring-boot-starter-logging 의존성 제거
- spring-boot-starter-log4j2의존성 추가
```xml
<!--   스프링부트 웹 의존성  -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

<!--     로거를 log4j2로 변경-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
```

# SpringBoot 활용 - 테스트
- spring-boot-starter-test를 추가
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
```
- junit, mockito, assertj 등 여러 의존성들이 추가된다.
- @SpringBootTest
  - @RunWith(SpringRunner.class)랑 같이 써야 함.
  - @SpringBootApplication 애노테이션이 붙은 클래스를 찾아서 모든 빈을 스캔하여 test 빈으로 등록을 해준다.
  - webEnvironment
    - SpringBootTest.WebEnvironment.MOCK
      - 내장톰캣 구동 안함
      - servlet이 mocking되어 구동되며 mocking된 servlet과 통신을 하려면 MockMvc라는 객체를 통해야 한다.
    - SpringBootTest.RANDOM_PORT
      - 내장톰캣 구동
      - TestRestTemplate 등 test용 webClient를 사용하여야한다.
  - @AutoConfigureMockMvc
    - MockMvc를 자동설정 해준다.
  - @MockBean
    - ApplicationContext에 등록된 빈을 Test시 해당 빈으로 교체해준다
    - 빈을 mocking하여 슬라이싱 테스트가 가능하다.
    - 모든 @Test마다 리셋된다.
  - WebTestClient
    - spring 5에서 추가된 WebTestClient
    - 비동기 테스트 클라이언트
    - webflux 의존성이 존재해야지만 사용할 수 있다.
  - 슬라이싱 테스트
    - 레이어별로 슬라이싱 테스트가 가능하다.
    - 각 레이어별로 빈이 등록된다.
    - @JsonTest jsonTest
    - @WebMvcTest ControllerTest
    - @WebFluxTest webFluxTest
    - @DataJpaTest RepositoryTest
```java
//컨트롤러 소스 작성
@RestController
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping("/hello")
    public String hello(){
        return "hello "+sampleService.getName();
    }
}
//서비스 소스 작성
@Service
public class SampleService {

    public String getName() {
        return "gimun";
    }
}

```
- intellij test case, test method 생성 단축키
  - 클래스명에 마우스 위치후 alt + enter ==> Test Case 생성
  - Test Case 소스 상에서 alt + insert ==> Test Method 생성
```java
//테스트코드
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
    @MockBean
    SampleService sampleService;

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
        when(sampleService.getName()).thenReturn("gimun");

//        String result = testRestTemplate.getForObject("/hello",String.class);
//        assertThat(result).isEqualTo("hello gimun");

        webTestClient.get().uri("/hello").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("hello gimun");
    }
}
```

# Spring Boot 활용 - 테스트 유틸
- OutPutCapture
  - Junit의 Rule을 확장한것
  - public으로 선언해야한다. 
  - log를 포함해서 콘솔에 찍히는 모든것을 캡쳐한다.
``java
// log 를 포함해서 콘솔에 찍히는것을 모두 캡쳐한다.
@Rule
public OutputCapture outputCapture = new OutputCapture();
        //toString 으로 나오는 내용들을 assertion
        //log나 sys out도 테스트 가능
        assertThat(outputCapture.toString())
                .contains("holoman")
                .contains("skip");
```

# Spring Boot 활용 - dev-tools
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```
- optional한 툴
- 캐시 설정을 개발환경에 맞게 변경해준다.
- 기본적으로 대부분 캐시관련 설정을 disable 시켜준다.
- 클래스패스에 존재하는 파일이 변경될때마다 자동응로 restart해준다.
    - 직접 재시작보다 빠르다 
        - 왜 ?
            - 스프링부트는 클래스로더를 2개 사용한다.
            - 1. BaseClassLoader (의존성을 읽어들임)
            - 2. RestartClassLoader클래스로더 (애플리케이션 코드를 읽어들임)
    - 릴로딩 보다 는 느리다.(JRebel같은것은 아니다.)
    - 리스타트하고싶지않은 리소스 추가
        - spring.devtools.restart.exclude
    - 리스타트 기능 off
        - spring.devtools.restart.enabled=false
- 라이브 릴로딩? ? 리스타트 했을 때 브라우저 자동 리프레시 하는 기능
    - 브라우저 플러그인을 설치해야한다.
    - 라이브 릴로딩 off 
        - spring.devtools.liveload.enabled=false
- 글로벌 설정
    - ~/.spring-boot-devtools.properties (1순위 외부설정파일의 위치 dev-tools (플러그인 의존성이 있을경우))

# Spring Boot - Web Mvc
- Spring Boot MVC 자동설정
    - Spring Boot 의 자동설정 에 의해 별다른 설정없이도 MVC 개발이 가능하다.
    - spring.factories > WebMvcAutoConfiguration class (자동설정 파일)

* HiddenHttpMethodFilter 
    - spring 3.0 부터 제공하는 filter
    - post 방식의 요청 파라메터로 _method 라는 값에 PUT,DELETE 등을 보내면
    - PUT , DELETE 등의 요청으로 매핑시켜주는 Filter 
* PutFormContentFilter
    - PUT , PATCH 도 contentType 이 form-data의 파라메터를 사용할수있도록 매핑해주는 Filter    

- MVC 설정 확장 (기본설정 +@)
    - @Configuration + WebMvcConfigurer

- MVC 재정의 (기본설정 덮어쓰기)
    - @Configuration + @EnableWebMvc
- https://docs.spring.io/spring/docs/5.0.7.RELEASE/spring-framework-reference/web.html#spring-web

# Spring Boot - HttpMessageConverter
- 스프링 프레임워크에서 지원하는 인터페이스로 HTTP 응답 본문을 객체로 변환하거나 , 객체를 응답본문으로 변환할때 사용한다.
- @RequestBody 또는 @ResponseBody와 함께 사용한다.
- 어떤 요청을 받았는지 , 어떤 응답을 보내야하는지에 따라 사용하는 MessageConverter가 다르다.
    - 요청이 Json 이라면 , JsonMessageConverter를 사용해서 요청을 변환해준다.
    - CompositionType(여러프로퍼티)이라면 기본으로 JsonMessageConverter를 사용한다.
    - String 일경우 StringMessageConverter를 사용한다.
- @RestController 일경우 모든 핸들러 메서드에 @ResponseBody 생략해도됨(@ResponseBody가 적용되어있는것과 동일)
- @RestController, @ResponseBody를 사용하지않을경우 ViewNameResolver를 사용해서 view를 찾으려고 할것이다.
- https://docs.spring.io/spring-framework/docs/5.0.7.RELEASE/spring-framework-reference/web.html#mvc-config-message-converters

# Spring Boot - ViewResolver
- 스프링부트가 제공하는 ContentsNegotiationViewResolver
  - 요청의 accpet Header에 따라 응답이 달라진다. (accpet Header: 클라이언트가 원하는 응답의 형식)
  - 어떠한 요청이들어오면 그 요청의 응답을 만들수있는 모든 뷰를 다 찾아냄
  - 최종적으로 accept 헤더와 뷰의 타입을 비교하여 선택함
  - accpetHeader 를 제공하지않는 클라이언트도 존재하는데 그럴경우 foramt이라는 매개변수를 사용한다.
    - /path?format=XML

- json 요청을 XML 로 응답받는 테스트코드
```java
    @Test
    public void createUser_JSON() throws Exception {
        String userJson = "{\"username\":\"gimun\",\"password\":\"123\"}";
        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON) // 요청
                .accept(MediaType.APPLICATION_XML) //응답
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) xpath("/User/name").string("gimun"))
                .andExpect((ResultMatcher) xpath("/User/password").string("1234"));
    }
```
- HttpMediaTypeNotAcceptableException 예외 발생
  - AcceptHeader를 요청과 함께 보내면 accpetHeader에 따라 MesageConverter를 다르게 사용하는데 XML MessageConverter가 등록되지않아 발생한 에러이다.
  - HttpMessageConvertersAutoConfiguration : HttpMessageConverter를 사용하는 설정을 자동설정해주는 빈
  ```java
    @Configuration(
      proxyBeanMethods = false
  )
  class JacksonHttpMessageConvertersConfiguration {
      JacksonHttpMessageConvertersConfiguration() {
      }

      @Configuration(
          proxyBeanMethods = false
      )
      @ConditionalOnClass({XmlMapper.class})
      @ConditionalOnBean({Jackson2ObjectMapperBuilder.class})
      protected static class MappingJackson2XmlHttpMessageConverterConfiguration {
          protected MappingJackson2XmlHttpMessageConverterConfiguration() {
          }

          @Bean
          @ConditionalOnMissingBean
          public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(Jackson2ObjectMapperBuilder builder) {
              return new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build());
          }
      }
  ```
  - HttpMessageConvertersAutoConfiguration.class > JacksonHttpMessageConvertersConfiguration.class
    - MappingJackson2XmlHttpMessageConverterConfiguration 처럼 xml을 컨버팅해주는 MessageConverter가 등록되는데 XmlMapper.class가 클래스패스에 있을때만 등록이 되도록 설정되어있음 
    - 현재는 XML 메시지를 컨버팅할수있는 Converter가 없는 상태.
    - XML 메시지 컨버터 의존성 추가
  ```xml
     <dependency>
      <groupId>com.fasterxml.jackson.dataformat</groupId>
      <artifactId>jackson-dataformat-xml</artifactId>
      <version>2.9.6</version>
     </dependency>
  ```

# Spring boot WebMvc - 정적 리소스 지원
- 정적 리소스란 ? 
   - resource가 이미 만들어져있고 그대로 응답으로 보내면 되는 리소스를 말한다.
   - js, html , css .. 등 
- 정적 리소스 매핑 /**
    - 아래의 4가지 클래스패스 경로에 존재하는 리소스들은 기본적으로 제공됨.
    - classpath:/static
    - classapth:/public
    - classapth:/resources/
    - classapth:/META-INF/resources/
    - 예) “/hello.html” => /static/hello.html
    - spring.mvc.static-path-pattern: 매핑설정 변경
    - spring.mvc.static-locations: 리소스를 찾을 위치 변경가능
> static,public,resources .. 하위에 hello.html이 존재할경우 해당파일을 응답으로 재공한다.

- http://localhost:8080/hello.html 로 요청을 보내면
- ResourceHttpRequestHandler 가 위 요청을 처리한다.
- LastModified 헤더 정보를 보고 304 응답을 보내기도한다.
  - 브라우저에서 해당 리소스가 언제 바뀌었는지 알고있음.
  - LastModified 정보가 바뀌지 않았을 경우 (If-Modified-Since == LastModified) 304 응답을 보내면서 해당 리소스를 다시 보내지는 않음.(훨씬 응답이 빨라짐)
  - 요청헤더의 If-Modified-Since 정보를 보고 If-Modified-Since 시간 이후에 바뀌었을 경우 새로 달라고 요청(200응답을 보냄)
  - 즉, LastModified 헤더 정보를 기반으로 caching된 데이터를 응답함.
- 리소스 찾을 위치 변경할 경우 리소스 핸들러를 추가하는 방식 추천(스프링부트가 지원하는 기본 방식을 유지하면서 새로운 리소스 맵핑설정 변경)
  - caching 전략을 따로 설정해주어야 함. 
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /*
     * 리소스 핸들러를 추가
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/m/") // 반드시 /로 끝나야함 안그러면 맵핑이 잘 안됨.
                .setCachePeriod(20);
    }
}
```
# Spring boot WebMvc - WebJar
- 스프링부트는  WebJar에 대한 기본 매핑을 제공한다.
- WebJar란 ? 
  - 클라이언트에서 사용하는 js 라이브러리들 을 jar파일로 추가하여 사용할수있다. ex)jquery,react,vue
> jQuery webJar 의존성추가
```xml
        <dependency>
            <groupId>org.webjars.bower</groupId>
            <artifactId>jquery</artifactId>
            <version>3.5.1</version>
        </dependency>
```
> webjar 사용
```html
<script src="/webjars/jquery/3.5.1/dist/jquery.min.js"></script>
<script>
    $(function(){
        alert("ready");
    })
</script>
```
- webjar locator core 를 의존성으로 추가하면 webjar의 버전을 생략할 수 있다.
> - resource Chaining 과 관련되어있음.(스프링프레임웤에 들어간 기능)
```xml
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>webjars-locator-core</artifactId>
    <version>0.37</version>
</dependency>
```
```html
<script src="/webjars/jquery/dist/jquery.min.js"></script>
<script>
    $(function(){
        alert("ready");
    })
</script>
```
# Spring Boot WebMvc - index page , favicon
- 웰컴 페이지
  - index.html 찾아 보고 있으면 제공.
  - index.템플릿 찾아 보고 있으면 제공.
  - 둘 다 없으면 에러 페이지.
- 파비콘
  - favicon.ico
  - 파이콘 만들기 https://favicon.io/
  - 파비콘이 안 바뀔 때?
    - https://stackoverflow.com/questions/2208933/how-do-i-force-a-favicon-refresh
    
# Spring Boot WebMvc - Thymeleaf
- 템플릿엔진
  - 주로 뷰를 만드는데 사용, 이메일 템플릿용도 등으로 사용
- 스프링 부트가 자동 설정을 지원하는 템플릿 엔진
  - FreeMarker
  - Groovy
  - Thymeleaf
  - Mustache
- JSP 권장하지 않는 이유
  - JAR 패키징할때는 동작하지않고, WAR패키징 해야 함.
  - Undertow는 JSP를 지원하지않음.
  - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-jsp-limitations
- Thymeleaf 사용하기
  - https://thymeleaf.org
  - https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html
  - 의존성 추가 : spring-boot-starter-thymeleaf
  - 템플릿 파일 위치:/src/main/resources/template  
  - 예제 : https://github.com/thymeleaf/thymeleafexamples-stsm/blob/3.0-master/src/main/webapp/WEB-INF/templates/seedstartermng.html
- Thymeleaf 테스트
```java
@Controller
public class SampleController {

    @GetMapping("/hellogimun")
    public String hello(Model model){
        model.addAttribute("name","gimun");
        return "hellogimun";
    }
}
```
```java
@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc; //가짜 서블릿 컨테이너

    @Test
    public void hello() throws Exception {
        //요청 "/hellogimun"
        //응답
        // - 모델 name : gimun
        // - 뷰 이름 : hellogimun
        mockMvc.perform(get("/hellogimun"))
                .andExpect(status().isOk())
                .andDo(print())  // 렌더링되는 결과를 print -> 타임리프를 사용하여 가능한것임 jsp는 실제 렌더링된 결과를 확인하기 힘듬
                .andExpect(view().name("hellogimun"))
                .andExpect(model().attribute("name","gimun"))
                .andExpect(content().string(containsString("gimun"))); // 본문 내용 테스트
    }
}
```
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1 th:text="${name}"> </h1>
</body>
</html>
```

