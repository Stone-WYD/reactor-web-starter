package com.wyd.reactor_web.mvc.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: reactor_web
 * @description: http 业务处理类
 * @author: Stone
 * @create: 2023-11-06 11:02
 **/
@ChannelHandler.Sharable
@Component
@Slf4j
public class HttpServerBusinessHandler2 extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // 通过编解码器把 byteBuf 解析成 FullHttpRequest
        if (msg instanceof FullHttpRequest) {

            //获取httpRequest
            FullHttpRequest httpRequest = (FullHttpRequest) msg;

            /*try {



            } finally {
                httpRequest.release();
            }*/
        }
    }
}
