package com.wyd.reactorweb.design.reactor.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.wyd.reactorweb.design.reactor.core.AsynRemoteChannel;
import com.wyd.reactorweb.design.reactor.core.AsynRemoteServiceProxy;
import com.wyd.reactorweb.design.reactor.factory.component.ResultStorageAndGain;
import com.wyd.reactorweb.design.reactor.pipeline.Handler;
import com.wyd.reactorweb.design.reactor.worker.AppWorker;
import com.wyd.reactorweb.design.reactor.worker.NetWorker;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: reactor-web-starter
 * @description: AsynRemoteChannel 构建工厂
 * @author: Stone
 * @create: 2023-11-28 18:10
 **/
public class SpringAsynRemoteChannelBuilderFactory {

    @Resource
    private NetWorker netWorker;

    @Resource
    private AppWorker appWorker;

    @Resource
    private ResultStorageAndGain resultStorageAndGain;

    private final Map<String, AsynRemoteChannel> channelMap = new HashMap<>();


    public void build(String beanName,
            AsynRemoteServiceProxy asynRemoteServiceProxy,
                      List<Handler> prepareHandlerList,
                      List<Handler> renderHandlerList) {
        // 根据 order 排序，越大的排在越后面
        List<Handler> prepareOrderList = prepareHandlerList.stream().sorted(Comparator.comparing(Handler::order)).collect(Collectors.toList());
        List<Handler> renderOrderList = renderHandlerList.stream().sorted(Comparator.comparing(Handler::order)).collect(Collectors.toList());
        // 创建 AsynRemoteChannel 对象
        AsynRemoteChannel asynRemoteChannel = new AsynRemoteChannel(netWorker, appWorker, resultStorageAndGain);
        asynRemoteChannel.bindRemoteService(asynRemoteServiceProxy);
        // 添加 Handler 对象
        if (CollectionUtil.isNotEmpty(prepareOrderList)) {
            prepareOrderList.forEach(asynRemoteChannel::addPrepareHandler);
        }
        if (CollectionUtil.isNotEmpty(renderHandlerList)) {
            renderOrderList.forEach(asynRemoteChannel::addResultRenderHandler);
        }
        asynRemoteChannel.start();
        channelMap.put(beanName, asynRemoteChannel);
    }

    public AsynRemoteChannel getAsynRemoteChannel(String beanName) {
        return channelMap.get(beanName);
    }
}
