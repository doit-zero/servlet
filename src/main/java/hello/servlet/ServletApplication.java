package hello.servlet;

import hello.servlet.web.springmvc.v1.SpringMemberFormControllerV1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 하위 패키지내에 서블릿을 찾아서 자동으로 등록해줌
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@ServletComponentScan
@SpringBootApplication
public class ServletApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServletApplication.class, args);
    }

//    @Bean
//    SpringMemberFormControllerV1 springMemberFormControllerV1() {
//        return new SpringMemberFormControllerV1();
//    }

}
