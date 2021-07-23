package com.yanan.framework;

import android.app.Activity;

import java.lang.annotation.Annotation;

public interface ClassHandler<T extends Annotation> {
    void process(Activity activity,Object instance,T annotation);
}
