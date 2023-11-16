package com.wyd.reactor_web.mvc.invoke.interfaces;

import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

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
                           HttpServletRequest httpRequest, @Nullable MyWebDataBinderFactory binderFactory) throws Exception;
}
