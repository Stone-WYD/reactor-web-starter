package com.wyd.reactor_web.mvc.mhandler.entity;

import cn.hutool.core.util.StrUtil;

import java.util.Set;

/**
 * @program: reactor_web
 * @description: 方法调用处理类
 * @author: Stone
 * @create: 2023-11-09 10:34
 **/
public class MyMethodHandler {

    private final MyInvoke myInvoke;

    private final Set<String> pathSet;

    private Object target;

    public MyMethodHandler(MyInvoke myInvoke, Set<String> pathSet, Object target) {
        this.myInvoke = myInvoke;
        this.pathSet = pathSet;
        this.target = target;
    }

    public Object invoke(String urlPath, Object[] args) {
        if (StrUtil.isBlank(urlPath)) {
            throw new RuntimeException("MyMethodHandler: 请传入有效url...");
        }
        if (pathSet.contains(urlPath)) {
            return myInvoke.invoke(urlPath, target, args);
        }
        return null;
    }

    public interface MyInvoke {
        Object invoke(String urlPath, Object target, Object[] args);
    }
}
