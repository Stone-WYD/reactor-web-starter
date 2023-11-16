package com.wyd.reactor_web.mvc.mhandler.entity;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @program: reactor_web
 * @description: 一个方法调用需要用到的东西
 * @author: Stone
 * @create: 2023-11-15 14:54
 **/
@Data
public class MyMethodInvokeGear {

    private MyMethodHandler myMethodHandler;

    private MyMethodParameter[] parameterArray;

    private Method method;

}
