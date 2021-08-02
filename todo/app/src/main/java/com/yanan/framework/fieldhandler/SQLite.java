package com.yanan.framework.fieldhandler;

import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE, ElementType.PARAMETER} )
public @interface SQLite {
    String value();
    int mode() default Context.MODE_PRIVATE;
}