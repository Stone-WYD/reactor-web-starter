package com.wyd.reactor_web.mvc.invoke.interfaces;

import com.wyd.reactor_web.mvc.invoke.request.MyHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;


/**
 * @program: reactor_web
 * @description:
 * @author: Stone
 * @create: 2023-11-15 16:21
 **/
public interface MyWebDataBinderFactory {

    WebDataBinder createBinder(MyHttpRequest httpRequest, @Nullable Object target, String objectName)
            throws Exception;
}
