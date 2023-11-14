package com.wyd.reactor_web.mvc.mhandler;

import com.wyd.reactor_web.annotation.MyRequestMapping;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import com.wyd.reactor_web.mvc.mhandler.interfaces.MyMethodParameterFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: reactor_web
 * @description: 方法参数集合，负责获取方法参数并封装起来
 * @author: Stone
 * @create: 2023-11-14 11:28
 **/
public class SpringMyMethodParameterFactory implements InstantiationAwareBeanPostProcessor, MyMethodParameterFactory {

    private final Map<String, MyMethodParameter[]> parameterMap = new HashMap<>();
    private final Map<String, Method> methodMap = new HashMap<>();

    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        MyRequestMapping classMapping = AnnotationUtils.findAnnotation(beanClass, MyRequestMapping.class);
        if (classMapping == null) {
            return null;
        }
        String value = classMapping.value();
        String classPath = value.startsWith("/") ? value : '/' + value;
        // 提取方法参数
        for (Method method : beanClass.getMethods()) {
            MyRequestMapping methodMapping = AnnotationUtils.findAnnotation(method, MyRequestMapping.class);
            if (methodMapping == null) {
                // 没有注解就跳过
                continue;
            }
            value = methodMapping.value();
            String methodPath = value.startsWith("/") ? value : '/' + value;
            String path = classPath + methodPath;
            methodMap.put(path, method);
            parameterMap.put(path, getMyMethodParameter(method));
        }
        return null;
    }

    private MyMethodParameter[] getMyMethodParameter(Method method) {
        String[] parameterNames = discoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        if (parameterNames.length != parameters.length) {
            throw new RuntimeException(method.getName() + "：获取到的参数名和参数数量不一致！" );
        }

        MyMethodParameter[] result = new MyMethodParameter[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            MyMethodParameter myMethodParameter = new MyMethodParameter();
            myMethodParameter.setParameterClass(parameter.getType());
            myMethodParameter.setAnnotations(parameter.getAnnotations());
            myMethodParameter.setParameterName(parameterNames[i]);
            result[i] = myMethodParameter;
        }
        return result;
    }

    @Override
    public Map<String, MyMethodParameter[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public Map<String, Method> getMethodMap() {
        return methodMap;
    }
}
