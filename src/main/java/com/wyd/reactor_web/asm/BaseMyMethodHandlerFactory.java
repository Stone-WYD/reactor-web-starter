package com.wyd.reactor_web.asm;

import cn.hutool.core.util.StrUtil;
import com.wyd.reactor_web.annotation.MyRequestMapping;
import com.wyd.reactor_web.asm.interfaces.MyMethodHandlerFactory;
import org.springframework.asm.Opcodes;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: reactor_web
 * @description: 获取方法调用类
 * @author: Stone
 * @create: 2023-11-10 16:26
 **/
public class BaseMyMethodHandlerFactory extends ClassLoader  implements MyMethodHandlerFactory, Opcodes {

    private List<MyMethodHandler> myMethodHandlers = new ArrayList<>();

    private AtomicInteger increNameNum = new AtomicInteger(0);

    @Override
    public List<MyMethodHandler> getMyMethodHandlers() {
        return myMethodHandlers;
    }

    public void addMyMethodHandler(Object target, Class<?> invokeClass) throws Exception {
        // 获取需要动态生成类的类名
        String generateClassName = "com/wyd/reactor_web/asm/tmp/MyInvoke" + increNameNum.addAndGet(1);
        // 获取动态类字节码
        byte[] code = GenerateClassUtil.generate(generateClassName, invokeClass);
        // 生成动态类的实例对象
        Class<?> aClass = defineClass(generateClassName, code, 0, code.length);
        MyMethodHandler.MyInvoke myInvoke = (MyMethodHandler.MyInvoke) aClass.getConstructor().newInstance();
        // 根据实例对象生成 MyMethodHandler
        MyMethodHandler myMethodHandler = new MyMethodHandler(myInvoke, getPathSet(invokeClass), target);
        // MyMethodHandler 加入 list
        myMethodHandlers.add(myMethodHandler);
    }

    private Set<String> getPathSet(Class<?> targetClass) {
        Set<String> result = new HashSet<>();
        MyRequestMapping classPathMapping = AnnotationUtils.findAnnotation(targetClass, MyRequestMapping.class);
        String classPath = "";
        // 获取类上的路径信息
        if (classPathMapping != null) {
            classPath = classPathMapping.value();
            if (!StrUtil.isBlank(classPath)) {
                classPath = classPath.startsWith("/") ? classPathMapping.value() : '/' + classPath ;
            }
        }
        // 获取方法上的路径信息
        Method[] declaredMethods = targetClass.getMethods();
        for (Method method : declaredMethods) {
            MyRequestMapping methodPathMapping = AnnotationUtils.findAnnotation(method, MyRequestMapping.class);
            if (methodPathMapping != null) {
                String methodPath = methodPathMapping.value();
                if (!StrUtil.isBlank(methodPath)) {
                    methodPath = methodPath.startsWith("/") ? methodPath : '/' + methodPath;
                }
                result.add(classPath + methodPath);
            }
        }
        return result;
    }
}
