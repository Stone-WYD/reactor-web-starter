package com.wyd.reactor_web.mvc.invoke.argument.resolver.self;

import com.wyd.reactor_web.mvc.invoke.interfaces.MyHandlerMethodArgumentResolver;
import com.wyd.reactor_web.mvc.invoke.interfaces.MyWebDataBinderFactory;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import java.lang.annotation.Annotation;

import static com.wyd.reactor_web.mvc.util.ConvertUtil.*;

/**
 * @program: reactor_web
 * @description: 普通参数解析器，解析get请求里uri上的参数
 * @author: Stone
 * @create: 2023-11-17 14:32
 **/
public class MyRequestParameterArgumentResolver implements  MyHandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MyMethodParameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();
        return annotations == null || annotations.length == 0;
    }

    @Override
    public Object resolveArgument(MyMethodParameter parameter, ModelAndViewContainer mavContainer,
                                  FullHttpRequest httpRequest, MyWebDataBinderFactory binderFactory) throws Exception {
        NativeWebRequest nativeWebRequest = convertFullHttpRequestToNativeWebRequest(httpRequest);
        ServletRequest servletRequest = convertFullHttpRequestToServletRequest(httpRequest);

        String parameterName = parameter.getParameterName();
        String parameterValue = nativeWebRequest.getParameter(parameterName);
        Class<?> parameterClass = parameter.getParameterClass();
        // 如果是基本类型，则取出来 String 类型转换后返回
        Object primitiveValue = convertStringToPrimitive(parameterClass, parameterValue);
        if (primitiveValue != null) return primitiveValue;
        // 使用工厂进行数据绑定
        Object target = parameterClass.getDeclaredConstructor().newInstance();
        WebDataBinder binder = binderFactory.createBinder(nativeWebRequest, target, parameterName);
        binder.bind(new ServletRequestParameterPropertyValues(servletRequest));
        return target;
    }



}
