package com.yanan.framework.message;

public class AsyncMessage {
    private Object top;
    private Object message;
    private boolean ignore;

    public AsyncMessage(Object top, Object message, boolean ignore) {
        this.top = top;
        this.message = message;
        this.ignore = ignore;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public Object getTop() {
        return top;
    }

    public void setTop(Object top) {
        this.top = top;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AsyncMessage{" +
                "top=" + top +
                ", message=" + message +
                ", ignore=" + ignore +
                '}';
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
