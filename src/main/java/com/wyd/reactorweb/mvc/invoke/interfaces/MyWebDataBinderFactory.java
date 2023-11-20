package com.wyd.reactorweb.mvc.invoke.interfaces;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;


/**
 * @program: reactor_web
 * @description:
 * @author: Stone
 * @create: 2023-11-15 16:21
 **/
public interface MyWebDataBinderFactory {

    WebDataBinder createBinder(NativeWebRequest nativeWebRequest, @Nullable Object target, String objectName)
            throws Exception;
}
