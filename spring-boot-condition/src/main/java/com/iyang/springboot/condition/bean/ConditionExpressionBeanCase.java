package com.iyang.springboot.condition.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Yang
 * 当前服务 : spring-boot-condition
 * @date 2021/1/5 / 15:47
 */

/**
 * 标签解析操作.
 */
@ConditionalOnExpression("'${server.port}' != '8888'")
@Component
public class ConditionExpressionBeanCase {

    public ConditionExpressionBeanCase(){
        System.out.println("ConditionExpressionBeanCase 无参数构造函数");
    }

}
