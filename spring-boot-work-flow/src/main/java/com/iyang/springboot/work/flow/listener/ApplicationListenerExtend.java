package com.iyang.springboot.work.flow.listener;

import com.sun.applet2.preloader.event.ApplicationExitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yang
 * 当前服务 : spring-boot-work-flow
 * @date 2021/1/5 / 15:03
 */

public class ApplicationListenerExtend implements ApplicationListener<ApplicationEvent>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationListenerExtend.class);

    /**
     * 这里可以看到,从控制台进来打印了九句 log , 也就说明是有九个 event 给发送出来了.
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        LOGGER.info(" 进入进来的 event 是 --->  {} " , event.getClass().toString());

    }

}
