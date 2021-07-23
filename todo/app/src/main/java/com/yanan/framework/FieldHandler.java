package com.yanan.framework;

import android.app.Activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface FieldHandler<T extends Annotation> {

    void process(Activity activity,Object instance,Field field, T annotaion);

}
