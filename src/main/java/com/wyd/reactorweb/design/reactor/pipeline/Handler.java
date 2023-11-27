package com.wyd.reactorweb.design.reactor.pipeline;


import com.wyd.reactorweb.design.reactor.core.ChannelContext;

/**
 * @program: TMSP
 * @description: 处理类
 * @author: Stone
 * @create: 2023-07-31 10:54
 **/
@FunctionalInterface
public interface Handler {
    void handle(ChannelContext channelContext);
}
