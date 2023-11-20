package com.wyd.reactorweb.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {

    private int code;

    private String message;

    public BaseException(int code, String name) {
        this.code = code;
        this.message = name;
    }
}
