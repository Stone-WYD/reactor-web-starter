package com.wyd.reactorweb.mvc.server.handler.business;

import com.wyd.reactorweb.mvc.invoke.NettyMyMethodInvokeHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @program: reactor_web
 * @description: http 业务处理类
 * @author: Stone
 * @create: 2023-11-06 11:02
 **/
@ChannelHandler.Sharable
@Slf4j
public class HttpServerBusinessHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Resource
    private NettyMyMethodInvokeHandler methodInvokeHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        //获取httpRequest
        try {
            methodInvokeHandler.invoke(ctx, msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
