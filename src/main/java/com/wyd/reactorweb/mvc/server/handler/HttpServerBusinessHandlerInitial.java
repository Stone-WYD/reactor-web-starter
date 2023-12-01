package com.wyd.reactorweb.mvc.server.handler;

import com.wyd.reactorweb.mvc.server.handler.business.HttpServerBusinessHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @program: reactor_web
 * @description: http 业务处理初始化类
 * @author: Stone
 * @create: 2023-11-06 11:02
 **/
@ChannelHandler.Sharable
@Slf4j
public class HttpServerBusinessHandlerInitial extends ChannelInitializer<SocketChannel> {

    @Resource
    private HttpServerBusinessHandler businessHandler;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(businessHandler);
    }
}
