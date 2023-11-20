package com.wyd.reactorweb.mvc.invoke.interfaces;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
* @Description: 请求来到时进行方法调用
* @Author: Stone
* @Date: 2023/11/15
*/
public interface MyMethodInvokeHandler {

    void invoke(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception;


}
