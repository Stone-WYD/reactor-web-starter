package com.wyd.reactorweb.design.reactor.core;

/**
 * @program: TMSP
 * @description: 异步调用结果
 * @author: Stone
 * @create: 2023-07-30 18:23
 **/
public class AsynReceptResult<T> {

    private final String errMsg;
    private final boolean isSuccess;
    private final T data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public T getData() {
        return data;
    }

    public AsynReceptResult(String errMsg, boolean isSuccess, T data) {
        this.errMsg = errMsg;
        this.isSuccess = isSuccess;
        this.data = data;
    }
}
