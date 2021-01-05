package com.iyang.springboot.work.flow.run;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Yang
 * 当前服务 : spring-boot-work-flow
 * @date 2021/1/5 / 14:53
 */

@Component
public class CommandLineRunnerExtend implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("调用 CommandLineRunnerExtend 的 run 方法");
    }

}
