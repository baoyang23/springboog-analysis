package com.iyang.spring.boot.apollo.analysis.bean;


public class FBean {


    public FBean(){
        System.out.println("FBean Init Function");
    }

    public void doIt(){
        System.out.println("调用到 FBean 中的 doIt 方法");
    }

}
