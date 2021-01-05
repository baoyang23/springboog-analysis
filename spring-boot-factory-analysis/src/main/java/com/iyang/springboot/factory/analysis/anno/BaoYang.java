package com.iyang.springboot.factory.analysis.anno;

import java.lang.annotation.*;

/**
 * @author Yang
 * 当前服务 : spring-boot-factory-analysis
 * @date 2021/1/5 / 17:55
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BaoYang {

    /**
     *
     * @return
     */
    String value() default "BaoYangShuaiB";

}
