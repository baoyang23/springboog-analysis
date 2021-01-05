package com.iyang.springboot.condition.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.stereotype.Component;

/**
 * @author Yang
 * 当前服务 : spring-boot-condition
 * @date 2021/1/5 / 15:55
 */

@ConditionalOnResource(resources = "application.properties")
@Component
public class ConditionalOnResourceCase {


    public ConditionalOnResourceCase(){
        System.out.println("ConditionalOnResourceCase 使用无参数构造函数被注入进来了");
    }

}
