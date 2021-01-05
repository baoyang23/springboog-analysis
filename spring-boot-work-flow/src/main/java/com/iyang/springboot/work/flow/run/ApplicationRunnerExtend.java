package com.iyang.springboot.work.flow.run;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Yang
 * 当前服务 : spring-boot-work-flow
 * @date 2021/1/5 / 14:52
 */

@Component
public class ApplicationRunnerExtend implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("调用到 ApplicationRunnerExtend 的 run 方法");
    }

}
