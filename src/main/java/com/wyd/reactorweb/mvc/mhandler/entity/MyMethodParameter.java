package com.wyd.reactorweb.mvc.mhandler.entity;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @program: reactor_web
 * @description: 方法参数封装
 * @author: Stone
 * @create: 2023-11-14 11:18
 **/
@Data
public class MyMethodParameter {

    private String parameterName;

    private Annotation[] annotations;

    private Class<?> parameterClass;

    @Override
    public String toString() {
        return "MyMethodParameter{" +
                "parameterName='" + parameterName + '\'' +
                ", annotations=" + Arrays.toString(annotations) +
                ", parameterClass=" + parameterClass +
                '}';
    }
}
