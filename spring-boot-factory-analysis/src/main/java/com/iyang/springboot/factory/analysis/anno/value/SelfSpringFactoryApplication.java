package com.iyang.springboot.factory.analysis.anno.value;

/**
 * @author Yang
 * 当前服务 : spring-boot-factory-analysis
 * @date 2021/1/5 / 17:56
 */
public class SelfSpringFactoryApplication {

    public SelfSpringFactoryApplication(){
        System.out.println("SelfSpringFactoryApplication 初始化构造函数");
    }

    @Override
    public String toString() {
        return "BaoYang帅b就是这么帅,无力回天.";
    }
}
