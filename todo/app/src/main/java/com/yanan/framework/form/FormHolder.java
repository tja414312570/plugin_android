package com.yanan.framework.form;

public interface FormHolder<T,K> {
    public void set(T view,K value);
    public K get(T view);
}
