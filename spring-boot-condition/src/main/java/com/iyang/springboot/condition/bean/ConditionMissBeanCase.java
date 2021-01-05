package com.iyang.springboot.condition.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.stereotype.Component;

/**
 * @author Yang
 * 当前服务 : spring-boot-condition
 * @date 2021/1/5 / 15:46
 */

@ConditionalOnMissingBean(value = FreeMarkerAutoConfiguration.class)
@Component
public class ConditionMissBeanCase {

    public ConditionMissBeanCase(){
        System.out.println("ConditionMissBeanCase 无参数构造函数");
    }

}
