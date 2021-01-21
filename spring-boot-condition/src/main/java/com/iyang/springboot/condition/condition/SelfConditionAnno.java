package com.iyang.springboot.condition.condition;


import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 *
 *
 * @author Yang
 */


@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(SelfCondition.class)
public @interface SelfConditionAnno {


}
