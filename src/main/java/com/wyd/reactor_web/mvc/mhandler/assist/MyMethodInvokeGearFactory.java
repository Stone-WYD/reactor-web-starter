package com.wyd.reactor_web.mvc.mhandler.assist;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodHandler;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodInvokeGear;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import com.wyd.reactor_web.mvc.mhandler.interfaces.MyMethodHandlerFactory;
import com.wyd.reactor_web.mvc.mhandler.interfaces.MyMethodParameterFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: reactor_web
 * @description: MyMethodInvokeGear获取工厂
 * @author: Stone
 * @create: 2023-11-15 14:56
 **/
public class MyMethodInvokeGearFactory {

    private final MyMethodHandlerFactory methodHandlerFactory;

    private final MyMethodParameterFactory parameterFactory;

    private final Map<String, MyMethodInvokeGear> myMethodInvokeGearMap = new ConcurrentHashMap<>();

    public MyMethodInvokeGearFactory(MyMethodHandlerFactory methodHandlerFactory, MyMethodParameterFactory parameterFactory) {
        this.methodHandlerFactory = methodHandlerFactory;
        this.parameterFactory = parameterFactory;
    }

    /**
    * @Description: 对数据进行校验，缓存获取到的数据
    * @Author: Stone
    * @Date: 2023/11/15
    */
    public MyMethodInvokeGear getMyMethodInvokeGearByUrl(String url) {
        MyMethodInvokeGear invokeGear = myMethodInvokeGearMap.get(url);
        if (invokeGear != null) {
            // 从缓存中获取
            return invokeGear;
        }
        List<MyMethodHandler> myMethodHandlers = methodHandlerFactory.getMyMethodHandlers();
        Map<String, MyMethodParameter[]> parameterMap = parameterFactory.getParameterMap();
        Map<String, Method> methodMap = parameterFactory.getMethodMap();

        if (CollectionUtil.isEmpty(methodMap) || CollectionUtil.isEmpty(parameterMap)
                || CollectionUtil.isEmpty(myMethodHandlers)) {
            throw new RuntimeException("MyMethodInvokeGearFactory: 并没有任何 url 对应的方法可以调用！");
        }

        MyMethodHandler methodHandler = null;
        for (MyMethodHandler myMethodHandler : myMethodHandlers) {
            if (myMethodHandler.enableInvoke(url)) {
                methodHandler = myMethodHandler;
            }
        }
        if (BeanUtil.isEmpty(methodHandler)) {
            throw new RuntimeException("MyMethodInvokeGearFactory: 没有找到 url 对应的方法可以调用！");
        }
        MyMethodParameter[] myMethodParameters = parameterMap.get(url);
        Method method = methodMap.get(url);
        MyMethodInvokeGear resultGear = new MyMethodInvokeGear();
        resultGear.setMethod(method);
        resultGear.setMyMethodHandler(methodHandler);
        resultGear.setParameterArray(myMethodParameters);
        myMethodInvokeGearMap.put(url, resultGear);
        return resultGear;
    }

}
