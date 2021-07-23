package com.yanan.framework;

import android.app.Activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface MethodHandler<T extends Annotation> {

    void process(Activity activity, Object instance, Method method, T annotaion);

}
