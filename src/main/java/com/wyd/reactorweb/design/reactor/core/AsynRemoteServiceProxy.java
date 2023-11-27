package com.wyd.reactorweb.design.reactor.core;


import com.wyd.reactorweb.common.AjaxResult;

import java.util.Map;

/**
 * @program: TMSP
 * @description: 异步远程调用代理类，为了解耦服务类和channel
 * @author: Stone
 * @create: 2023-07-31 13:44
 **/
public abstract class AsynRemoteServiceProxy<T> {

    public abstract AjaxResult<String> call(ChannelContext channelContext);

    public abstract AsynReceptResult<Map<String, AjaxResult<T>>> requestReceipt();

}
