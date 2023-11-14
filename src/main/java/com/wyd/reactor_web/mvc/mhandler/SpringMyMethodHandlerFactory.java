package com.wyd.reactor_web.mvc.mhandler;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.wyd.reactor_web.mvc.util.MapUtil.getPathSet;

/**
 * @program: reactor_web
 * @description: BaseMyMethodHandlerFactory关于Spring的实现，实现一些spring接口，并注入spring容器，
 * 在项目启动时将所有 MyMethodHandler 准备好。需要注意此处 @Order(value = Integer.MIN_VALUE) 与 aop
 * 的后处理器顺序值是一样的，但是该后处理器需要在 aop 之后执行，所以此处需要一个后工厂处理器进行一些处理
 * @author: Stone
 * @create: 2023-11-13 13:50
 **/
@Order(value = Integer.MIN_VALUE)
public class SpringMyMethodHandlerFactory extends BaseMyMethodHandlerFactory implements InstantiationAwareBeanPostProcessor {

    private final Set<String> allPathSet = new HashSet<>();

    private final Map<String, Class<?>> beanClassMap = new HashMap<>();

    /**
    * @Description: 实例化单例之前进行的操作，为了尽早校验 MyRequestMapping 上的 url 是否重复
    * @Author: Stone
    * @Date: 2023/11/13
    */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        Set<String> pathSet = getPathSet(beanClass);
        // 为空不做处理
        if (CollectionUtil.isEmpty(pathSet)) {
            return null;
        }
        // 进行非空判断
        for (String path : pathSet) {
            if (allPathSet.contains(path)) {
                throw new RuntimeException("SpringMyMethodHandlerFactory::postProcessBeforeInstantiation(Class<?> beanClass, String beanName): " +
                        "有重复的 MyRequestMapping 映射路径！");
            } else {
                allPathSet.add(path);
                beanClassMap.put(beanName, beanClass);
            }
        }
        return null;
    }

    /**
    * @Description: 初始化之后的操作，生成 MyMethodHandler 放入myMethodHandlers中
    * @Author: Stone
    * @Date: 2023/11/13
    */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 不对 bean 做任何处理，只是通过 bean 生成 MyMethodHandler 实例
        if (beanClassMap.get(beanName) == null) {
            return bean;
        }
        try {
            addMyMethodHandler(bean, beanClassMap.get(beanName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    /**
    * @Description: 此处不实现内容，myMethodHandlers 初始化逻辑在 postProcessAfterInitialization 方法中完成
    * @Author: Stone
    * @Date: 2023/11/13
    */
    @Override
    void init() {
    }

}
