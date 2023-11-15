package com.wyd.reactor_web.mvc.invoke.interfaces;

import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @program: reactor_web
 * @description: 参数处理器
 * @author: Stone
 * @create: 2023-11-15 15:58
 **/
public interface MyHandlerMethodArgumentResolver {

    boolean supportsParameter(MyMethodParameter parameter);

    @Nullable
    Object resolveArgument(MyMethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                           FullHttpRequest httpRequest, @Nullable MyWebDataBinderFactory binderFactory) throws Exception;
}
