package com.wyd.reactorweb.mvc.mhandler.interfaces;

import com.wyd.reactorweb.mvc.mhandler.entity.MyMethodParameter;

import java.lang.reflect.Method;
import java.util.Map;

public interface MyMethodParameterFactory {

    Map<String, MyMethodParameter[]> getParameterMap();

    Map<String, Method> getMethodMap();
}
