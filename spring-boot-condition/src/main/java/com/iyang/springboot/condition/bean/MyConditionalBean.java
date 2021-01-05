package com.iyang.springboot.condition.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Yang
 * 当前服务 : spring-boot-condition
 * @date 2021/1/5 / 15:50
 */

@Conditional(MyCondition.class)
@Component
public class MyConditionalBean {

    public MyConditionalBean(){
        System.out.println("MyConditionalBean 无参数构造函数");
    }

}

class MyCondition implements Condition {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyCondition.class);

    /**
     * 这里返回的 true 和 false 会决定上面的  MyConditionalBean 会不会注入到 Spring 容器中来.
     * 如果返回的是 true 的话, 就会注入到 Spring 容器中来.
     * 如果返回的是 false 的话, 就不会.
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        LOGGER.info(" ----> {} " , Arrays.toString(context.getEnvironment().getActiveProfiles()));
        LOGGER.info(" ----> {} " , context.getEnvironment().getProperty("server.port"));

        return true;
    }

}
