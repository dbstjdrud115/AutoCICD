package com.ll.demo03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Demo03Application {


    public static void main(String[] args) {
        System.out.println("변경여부 테스트~~");
        SpringApplication.run(Demo03Application.class, args);
    }

}
