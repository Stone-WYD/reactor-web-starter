package com.wyd.reactorweb.design.reactor.core;

/**
 * @program: TMSP
 * @description: 异步调用结果
 * @author: Stone
 * @create: 2023-07-30 18:23
 **/
public class AsynReceptResult<T> {

    private String errMsg;
    private boolean isSuccess;
    private T data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public T getData() {
        return data;
    }
}
