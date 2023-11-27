package com.wyd.reactorweb.design.reactor.pipeline.event;


import com.wyd.reactorweb.design.reactor.core.ChannelContext;

/**
 * @program: TMSP
 * @description:
 * @author: Stone
 * @create: 2023-07-31 11:10
 **/
public class BaseEvent {

    private ChannelContext channelContext;

    public ChannelContext getChannelContext() {
        return channelContext;
    }

    public void setChannelContext(ChannelContext channelContext) {
        this.channelContext = channelContext;
    }

    public BaseEvent(ChannelContext channelContext) {
        this.channelContext = channelContext;
    }
}
