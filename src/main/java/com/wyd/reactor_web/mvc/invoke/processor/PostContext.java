package com.wyd.reactor_web.mvc.invoke.processor;

public class PostContext<T> {

    private final T t;

    public PostContext(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }


}
