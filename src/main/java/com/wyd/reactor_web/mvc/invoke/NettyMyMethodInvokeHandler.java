package com.wyd.reactor_web.mvc.invoke;

import com.wyd.reactor_web.mvc.invoke.interfaces.MyMethodInvokeHandler;
import com.wyd.reactor_web.mvc.mhandler.interfaces.MyMethodHandlerFactory;
import com.wyd.reactor_web.mvc.mhandler.interfaces.MyMethodParameterFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @program: reactor_web
 * @description: 使用 Spring 和 Netty 完成请求调用的功能
 * @author: Stone
 * @create: 2023-11-15 14:28
 **/
public class NettyMyMethodInvokeHandler implements MyMethodInvokeHandler {

    public NettyMyMethodInvokeHandler(MyMethodHandlerFactory methodHandlerFactory, MyMethodParameterFactory parameterFactory) {

    }

    @Override
    public void invoke(ChannelHandlerContext ctx, FullHttpRequest msg) {

    }

}
