package com.yanan.framework.message;

public interface MessageHandler<T> {
    void onMessage(T message);
}
