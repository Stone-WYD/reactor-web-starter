package com.wyd.reactor_web.mvc.util;

import cn.hutool.core.util.StrUtil;
import com.wyd.reactor_web.annotation.MyRequestMapping;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: reactor_web
 * @description: 映射工具类
 * @author: Stone
 * @create: 2023-11-13 18:02
 **/
public class MapperUtil {

    public static Set<String> getPathSet(Class<?> targetClass) {
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
