package com.wyd.reactor_web.mvc.mhandler.interfaces;

import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;

import java.lang.reflect.Method;
import java.util.Map;

public interface MyMethodParameterFactory {

    Map<String, MyMethodParameter[]> getParameterMap();

    Map<String, Method> getMethodMap();
}
