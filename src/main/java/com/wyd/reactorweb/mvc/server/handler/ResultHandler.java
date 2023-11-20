package com.wyd.reactorweb.mvc.server.handler;

import com.alibaba.fastjson.JSON;
import com.wyd.reactorweb.common.AjaxResult;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @program: reactor_web
 * @description: 结果处理类
 * @author: Stone
 * @create: 2023-11-07 09:03
 **/
@ChannelHandler.Sharable
public class ResultHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof AjaxResult<?>) {
            System.out.println("获取到结果了~");
            // 结果序列化
            AjaxResult<?> ajaxResult = (AjaxResult<?>) msg;
            String responseMsg = JSON.toJSONString(ajaxResult);

            // 返回 response
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(responseMsg, CharsetUtil.UTF_8)
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");

            // 发送给客户端后关闭连接
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            super.write(ctx, msg, promise);
        }

    }
}
