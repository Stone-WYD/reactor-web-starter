package com.wyd.reactor_web.mvc.invoke.argument.resolver;

import com.wyd.reactor_web.mvc.invoke.interfaces.MyHandlerMethodArgumentResolver;
import com.wyd.reactor_web.mvc.invoke.interfaces.MyWebDataBinderFactory;
import com.wyd.reactor_web.mvc.invoke.request.MyHttpRequest;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: reactor_web
 * @description: 参数解析器组合类
 * @author: Stone
 * @create: 2023-11-16 18:12
 **/
public class MyHandlerMethodArgumentResolverComposite implements BeanPostProcessor, MyHandlerMethodArgumentResolver {

    private final List<MyHandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    // 缓存
    private final Map<MyMethodParameter, MyHandlerMethodArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(256);

    /**
    * @Description: 此处不考虑后处理器会被动态代理增强的情况
    * @Author: Stone
    * @Date: 2023/11/17
    */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 将后处理器加入到参数处理器中
        if (bean instanceof MyHandlerMethodArgumentResolver) {
            resolvers.add((MyHandlerMethodArgumentResolver) bean);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public boolean supportsParameter(MyMethodParameter parameter) {
        for (MyHandlerMethodArgumentResolver resolver : resolvers) {
            if (resolver.supportsParameter(parameter)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MyMethodParameter parameter, ModelAndViewContainer mavContainer, MyHttpRequest httpRequest, MyWebDataBinderFactory binderFactory) throws Exception {
        MyHandlerMethodArgumentResolver argumentResolver = getArgumentResolver(parameter);
        if (argumentResolver == null) {
            throw new RuntimeException("Unsupported parameter type [" +
                    parameter.getParameterClass().getName() + "]. supportsParameter should be called first.");
        }
        return argumentResolver.resolveArgument(parameter, mavContainer, httpRequest, binderFactory);
    }

    @Nullable
    private MyHandlerMethodArgumentResolver getArgumentResolver(MyMethodParameter parameter) {
        MyHandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
        if (result == null) {
            for (MyHandlerMethodArgumentResolver resolver : this.resolvers) {
                if (resolver.supportsParameter(parameter)) {
                    result = resolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }
}
