package com.wyd.reactorweb.design.reactor.pipeline.event;


import com.wyd.reactorweb.design.reactor.core.ChannelContext;

/**
 * @program: TMSP
 * @description: 结果渲染事件
 * @author: Stone
 * @create: 2023-07-31 11:41
 **/
public class ResultRenderEvent extends BaseEvent{

    public ResultRenderEvent(ChannelContext channelContext) {
        super(channelContext);
    }
}
