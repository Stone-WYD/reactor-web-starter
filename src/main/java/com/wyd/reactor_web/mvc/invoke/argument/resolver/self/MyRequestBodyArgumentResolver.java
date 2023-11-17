package com.wyd.reactor_web.mvc.invoke.argument.resolver.self;

import com.alibaba.fastjson.JSON;
import com.wyd.reactor_web.annotation.param.MyReqeustBody;
import com.wyd.reactor_web.mvc.invoke.interfaces.MyHandlerMethodArgumentResolver;
import com.wyd.reactor_web.mvc.invoke.interfaces.MyWebDataBinderFactory;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;

/**
 * @program: reactor_web
 * @description: 请求体转换参数处理类
 * @author: Stone
 * @create: 2023-11-17 16:23
 **/
public class MyRequestBodyArgumentResolver implements MyHandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MyMethodParameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();
        if (annotations != null || annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof MyReqeustBody) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MyMethodParameter parameter, ModelAndViewContainer mavContainer, FullHttpRequest httpRequest, MyWebDataBinderFactory binderFactory) {
        String content = httpRequest.content().toString(CharsetUtil.UTF_8);
        return JSON.parseObject(content, parameter.getParameterClass());
    }
}
