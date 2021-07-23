package com.yanan.framework.service;

import android.app.Activity;

public interface InstanceHandler<T>{
    T instance(Activity activity, Class<T> instanceType);
}
