package com.wyd.reactor_web.mvc.invoke.interfaces;

import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;

/**
 * @program: reactor_web
 * @description:
 * @author: Stone
 * @create: 2023-11-15 16:21
 **/
interface MyWebDataBinderFactory {

    WebDataBinder createBinder(FullHttpRequest httpRequest, @Nullable Object target, String objectName)
            throws Exception;

}
