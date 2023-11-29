package com.wyd.reactorweb.test.reactor.channel;

import com.wyd.reactorweb.annotation.ChannelInfo;
import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.design.reactor.core.AsynReceptResult;
import com.wyd.reactorweb.design.reactor.core.AsynRemoteServiceProxy;
import com.wyd.reactorweb.design.reactor.core.ChannelContext;
import com.wyd.reactorweb.test.reactor.entity.SendInfo;
import com.wyd.reactorweb.test.reactor.service.impl.RemoteMessageSendServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: reactor-web-starter
 * @description: service 转 channel 服务类适配器类
 * @author: Stone
 * @create: 2023-11-29 10:59
 **/
@Component
@ChannelInfo
public class MessageSendServiceAsynAdaptor extends AsynRemoteServiceProxy<Boolean> {

    @Resource
    private RemoteMessageSendServiceImpl remoteMessageSendService;

    @Override
    public AjaxResult<String> call(ChannelContext channelContext) {
        SendInfo sendInfo = ((SendInfo) channelContext.getParamMap().get("sendInfo"));
        return remoteMessageSendService.send(sendInfo);
    }

    @Override
    public AsynReceptResult<Map<String, AjaxResult<Boolean>>> requestReceipt() {
        return remoteMessageSendService.getResultList();
    }
}
