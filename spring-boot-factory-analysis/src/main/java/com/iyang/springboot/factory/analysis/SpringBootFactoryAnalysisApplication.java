package com.iyang.springboot.factory.analysis;

import com.iyang.springboot.factory.analysis.anno.BaoYang;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author Yang
 */
@SpringBootApplication
public class SpringBootFactoryAnalysisApplication {

    public static void main(String[] args)  throws ClassNotFoundException ,
            InstantiationException, IllegalAccessException {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBootFactoryAnalysisApplication.class, args);

        List<String> baoYangNameList =
                SpringFactoriesLoader.loadFactoryNames(BaoYang.class, null);
        String beanName = baoYangNameList.get(0);
        Class<?> aClass = Class.forName(beanName);
        Object instance = aClass.newInstance();
        System.out.println(instance.toString());

        context.getBeanFactory().registerSingleton("baoyang",instance);

        System.out.println("--------- 华丽分割线  -----");
        Object baoyang = context.getBean("baoyang");
        System.out.println(baoyang);

    }

}
