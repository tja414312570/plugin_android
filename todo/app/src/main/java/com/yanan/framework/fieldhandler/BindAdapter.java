package com.yanan.framework.fieldhandler;

import com.yanan.framework.After;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@After(Service.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD} )
public @interface BindAdapter {
    int value();
}
