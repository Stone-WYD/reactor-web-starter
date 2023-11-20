package com.wyd.reactorweb.mvc.listeners;

import com.wyd.reactorweb.autil.ApplicationContextUtil;
import com.wyd.reactorweb.mvc.mhandler.assist.MyMethodInvokeGearFactory;
import com.wyd.reactorweb.mvc.mhandler.interfaces.MyMethodParameterFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Set;

/**
 * @program: reactor_web
 * @description: 容器刷新后，进行一些初始化操作的监听器
 * @author: Stone
 * @create: 2023-11-20 11:30
 **/
public class MyInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 完成容器刷新后，从容器中获取 MyMethodInvokeGearFactory 进行路径映射内容的缓存
        MyMethodInvokeGearFactory invokeGearFactory = ApplicationContextUtil.getBeanOfType(MyMethodInvokeGearFactory.class);
        // 1. 通过 SpringMyMethodParameterFactory 获取到所有的 url
        MyMethodParameterFactory parameterFactory = ApplicationContextUtil.getBeanOfType(MyMethodParameterFactory.class);
        Set<String> urlKeySet = parameterFactory.getMethodMap().keySet();
        // 2. 缓存所有调用信息
        for (String url : urlKeySet) {
            invokeGearFactory.getMyMethodInvokeGearByUrl(url);
        }
    }
}
