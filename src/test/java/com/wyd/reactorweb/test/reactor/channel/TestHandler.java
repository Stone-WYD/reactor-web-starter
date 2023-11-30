package com.wyd.reactorweb.test.reactor.channel;

import com.wyd.reactorweb.design.reactor.core.ChannelContext;
import com.wyd.reactorweb.design.reactor.pipeline.Handler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @program: reactor-web-starter
 * @description: 测试Handler类
 * @author: Stone
 * @create: 2023-11-29 14:31
 **/
@Component
public class TestHandler implements Handler {

    @Override
    public void handle(ChannelContext channelContext) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) channelContext.getContextMap().get("channelHandlerContext");
        ctx.write(channelContext.getAsynReceptResult());
    }
}
