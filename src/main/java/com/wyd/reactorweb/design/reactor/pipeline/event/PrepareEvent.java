package com.wyd.reactorweb.design.reactor.pipeline.event;


import com.wyd.reactorweb.design.reactor.core.ChannelContext;

/**
 * @program: TMSP
 * @description: 预处理事件
 * @author: Stone
 * @create: 2023-07-31 11:41
 **/
public class PrepareEvent extends BaseEvent{
    public PrepareEvent(ChannelContext channelContext) {
        super(channelContext);
    }
}
