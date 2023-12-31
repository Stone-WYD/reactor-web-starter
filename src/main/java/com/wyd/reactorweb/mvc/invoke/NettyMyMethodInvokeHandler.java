package com.wyd.reactorweb.mvc.invoke;

import cn.hutool.core.bean.BeanUtil;
import com.wyd.reactorweb.mvc.invoke.interfaces.MyHandlerMethodArgumentResolver;
import com.wyd.reactorweb.mvc.invoke.interfaces.MyMethodInvokeHandler;
import com.wyd.reactorweb.mvc.invoke.interfaces.MyWebDataBinderFactory;
import com.wyd.reactorweb.mvc.invoke.processor.SpringInvokePostProcessorContainer;
import com.wyd.reactorweb.mvc.mhandler.assist.MyMethodInvokeGearFactory;
import com.wyd.reactorweb.mvc.mhandler.entity.MyMethodHandler;
import com.wyd.reactorweb.mvc.mhandler.entity.MyMethodInvokeGear;
import com.wyd.reactorweb.mvc.mhandler.entity.MyMethodParameter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @program: reactor_web
 * @description: 使用 Spring 和 Netty 完成请求调用的功能
 * @author: Stone
 * @create: 2023-11-15 14:28
 **/
public class NettyMyMethodInvokeHandler implements MyMethodInvokeHandler {

    private final MyMethodInvokeGearFactory myMethodInvokeGearFactory;

    private final MyHandlerMethodArgumentResolver argumentResolver;

    private final MyWebDataBinderFactory binderFactory;

    private final SpringInvokePostProcessorContainer springInvokePostProcessorContainer;

    public NettyMyMethodInvokeHandler(MyMethodInvokeGearFactory myMethodInvokeGearFactory,
                                      MyHandlerMethodArgumentResolver argumentResolver,
                                      MyWebDataBinderFactory binderFactory,
                                      SpringInvokePostProcessorContainer invokePostProcessorContainer
                                      ) {
        this.myMethodInvokeGearFactory = myMethodInvokeGearFactory;
        this.argumentResolver = argumentResolver;
        this.binderFactory = binderFactory;
        this.springInvokePostProcessorContainer = invokePostProcessorContainer;
    }

    @Override
    public void invoke(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        // 请求地址
        String path = httpRequest.uri();
        if (path.indexOf('?') != -1) {
            path = path.substring(0, path.indexOf('?'));
        }

        // 方法调用需要的东西
        MyMethodInvokeGear invokeGear = myMethodInvokeGearFactory.getMyMethodInvokeGearByUrl(path);

        MyMethodHandler myMethodHandler = invokeGear.getMyMethodHandler();
        MyMethodParameter[] myMethodParameters = invokeGear.getParameterArray();
        Object[] parameters = new Object[myMethodParameters.length];

        // 组合模式：准备方法调用参数
        for (int i = 0; i < myMethodParameters.length; i++) {
            MyMethodParameter myMethodParameter = myMethodParameters[i];

            if (Channel.class.isAssignableFrom(myMethodParameter.getParameterClass())) {
                // 如果传参是要 Channel，从 ctx 中获取后传入
                parameters[i] = ctx.channel();
                continue;
            }

            /*
            // 不再给 controller 传入 ctx
            if (ChannelHandlerContext.class.isAssignableFrom(myMethodParameter.getParameterClass())) {
                // 如果传参是要 ChannelHandlerContext，则直接传入
                parameters[i] = ctx;
                continue;
            }
            */

            if (argumentResolver.supportsParameter(myMethodParameter)) {
                Object o = argumentResolver.resolveArgument(myMethodParameter, null, httpRequest, binderFactory);
                parameters[i] = o;
            } else {
                throw new RuntimeException("NettyMyMethodInvokeHandler::invoke,参数没有合适的处理器处理！");
            }
        }

        InvokePostPrcessorContext context = new InvokePostPrcessorContext();
        context.setReturnClass(invokeGear.getReturnClass());
        context.setChannelHandlerContext(ctx);
        context.setMyMethodParameters(myMethodParameters);
        context.setParameters(parameters);
        // 后处理器方式：方法调用前的操作
        boolean handleFlag = springInvokePostProcessorContainer.handleBefore(context);

        // 方法调用
        if (handleFlag) {
            Object result = myMethodHandler.invoke(path, parameters);
            if (BeanUtil.isNotEmpty(result)) {
                context.setInvokeResult(result);
            }
        }

        // 后处理器方法：方法调用后的操作
        springInvokePostProcessorContainer.handleAfter(context);

    }
}
