package com.wyd.reactorweb.mvc.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import javax.annotation.Resource;

/**
 * @program: reactor_web
 * @description: http处理初始化类
 * @author: Stone
 * @create: 2023-11-06 11:01
 **/
public class HttpServerHandlerInitial extends ChannelInitializer<SocketChannel> {

    /*@Resource
    private HttpServerTestHandler businessHandler;*/

    @Resource
    private HttpServerBusinessHandler businessHandler;

    @Resource
    private ResultHandler resultHandler;

    @Override
    protected void initChannel(SocketChannel ch) {

        ChannelPipeline pipeline = ch.pipeline();

        //http请求编解码器,请求解码，响应编码
        pipeline.addLast("serverCodec", new HttpServerCodec());
        //http请求报文聚合为完整报文，最大请求报文为10M
        pipeline.addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
        //响应报文压缩
        pipeline.addLast("compress", new HttpContentCompressor());
        // 结果处理
        pipeline.addLast("resultHandler", resultHandler);
        //业务处理handler
        pipeline.addLast("serverBusinessHandler", businessHandler);
    }
}
