package com.wyd.reactor_web.mvc.invoke.interfaces;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: reactor_web
 * @description:
 * @author: Stone
 * @create: 2023-11-15 16:21
 **/
public interface MyWebDataBinderFactory {

    WebDataBinder createBinder(HttpServletRequest httpRequest, @Nullable Object target, String objectName)
            throws Exception;

}
