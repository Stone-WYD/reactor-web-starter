package com.wyd.reactorweb.design.reactor.pipeline.event;


import com.wyd.reactorweb.design.reactor.core.ChannelContext;

/**
 * @program: TMSP
 * @description: 远程调用事件
 * @author: Stone
 * @create: 2023-07-31 11:42
 **/
public class RemoteRequestEvent extends BaseEvent{


    public RemoteRequestEvent(ChannelContext channelContext) {
        super(channelContext);
    }
}
