package com.yanan.framework.fieldhandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取试图组件 value为试图id
 * 当value为0，表明获取上下文试图
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER} )
public @interface Views {
    int value();
    boolean required() default false;
}