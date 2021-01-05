package com.iyang.springboot.work.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootWorkFlowApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootWorkFlowApplication.class, args);

        Object baoyang = context.getBean("baoyang");
        System.out.println(baoyang);

    }

}
