package com.atguigu.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")
public class ServiceHoseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHoseApplication.class,args);
    }


}
