package com.iyang.springboot.work.flow.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yang
 * 当前服务 : spring-boot-work-flow
 * @date 2021/1/5 / 14:56
 */

public class ApplicationContextExtend implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 先判断 beanFactory 有没有 baoyang, 如果没有的话,就注册一个进去.
     *
     * 这里就进来一次 , 通过 atomicInteger 打印出来的大小可以看出来.
     * @param applicationContext
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        Object baoyang = applicationContext.getBeanFactory().getSingleton("baoyang");
        if (baoyang == null) {
            applicationContext.getBeanFactory().registerSingleton("baoyang","GavinYang");
        }

        int i = atomicInteger.incrementAndGet();
        System.out.println("i value is ---> " + i);
    }

}
