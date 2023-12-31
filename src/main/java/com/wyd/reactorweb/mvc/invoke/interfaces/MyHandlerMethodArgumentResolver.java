package com.wyd.reactorweb.mvc.invoke.interfaces;

import com.wyd.reactorweb.mvc.mhandler.entity.MyMethodParameter;
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

    // @Description: 此处的 ModelAndViewContainer 是预留的，暂时还用不到
    @Nullable
    Object resolveArgument(MyMethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                           FullHttpRequest httpRequest, @Nullable MyWebDataBinderFactory binderFactory) throws Exception;
}
