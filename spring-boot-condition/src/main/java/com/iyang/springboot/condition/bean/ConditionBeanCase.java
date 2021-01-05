package com.iyang.springboot.condition.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.stereotype.Component;

/**
 * @author Yang
 * 当前服务 : spring-boot-condition
 * @date 2021/1/5 / 15:37
 */


@ConditionalOnBean(value = FreeMarkerAutoConfiguration.class)
@Component
public class ConditionBeanCase {

    public ConditionBeanCase(){
        System.out.println("ConditionBeanCase 无参数构造函数");
    }

}
