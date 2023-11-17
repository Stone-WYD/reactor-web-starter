package com.wyd.reactor_web.mvc.invoke.argument.binder;

import com.wyd.reactor_web.annotation.MyControllerAdvice;
import com.wyd.reactor_web.annotation.MyInitBinder;
import com.wyd.reactor_web.autil.ApplicationContextUtil;
import com.wyd.reactor_web.mvc.invoke.interfaces.MyWebDataBinderFactory;
import com.wyd.reactor_web.mvc.invoke.request.MyHttpRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: reactor_web
 * @description: web数据绑定工厂
 * @author: Stone
 * @create: 2023-11-17 10:23
 **/
public class SpringMyWebDataBinderFactory implements MyWebDataBinderFactory, InstantiationAwareBeanPostProcessor {

    private volatile ServletRequestDataBinderFactory factory;

    private final List<InvocableHandlerMethod> invocableHandlerMethodList = new ArrayList<>();

    private final Map<String, Method> methodMap = new HashMap<>();
    /**
    * @Description: 实例化之前的操作，根据注解获取方法信息创建 InvocableHandlerMethod 备用
    * @Author: Stone
    * @Date: 2023/11/17
    */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        MyControllerAdvice advice = AnnotationUtils.findAnnotation(beanClass, MyControllerAdvice.class);
        if (advice != null) {
            for (Method method : beanClass.getMethods()) {
                MyInitBinder initBinder = AnnotationUtils.findAnnotation(method, MyInitBinder.class);
                if (initBinder != null) {
                    methodMap.put(beanName, method);
                }
            }
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method method = methodMap.get(beanName);
        if (method != null) {
            InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(bean, method);
            invocableHandlerMethodList.add(invocableHandlerMethod);
        }
        return bean;
    }

    @Override
    public WebDataBinder createBinder(MyHttpRequest httpRequest, Object target, String objectName) {
        initInitializer();
        try {
            return factory.createBinder(httpRequest, target, objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initInitializer() {
        if (factory == null) {
            synchronized (this) {
                if (factory == null) {
                    FormattingConversionService service = new FormattingConversionService();
                    // 在容器中获取转换器
                    List<Formatter> formatterList = ApplicationContextUtil.getBeansOfType(Formatter.class);
                    for (Formatter<?> formatter : formatterList) {
                        service.addFormatter(formatter);
                    }
                    ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
                    initializer.setConversionService(service);
                    factory = new ServletRequestDataBinderFactory(invocableHandlerMethodList, initializer);
                }
            }
        }
    }

}
