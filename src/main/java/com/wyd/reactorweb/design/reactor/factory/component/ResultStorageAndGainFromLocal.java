package com.wyd.reactorweb.design.reactor.factory.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wyd.reactorweb.config.property.CoreProperties;
import com.wyd.reactorweb.config.property.core.ResultStorage;
import com.wyd.reactorweb.design.reactor.core.ChannelContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @program: reactor-web-starter
 * @description: 使用本地存储 channel 调用结果，不使用泛型，因为要存储所有 channel 中间结果
 * @author: Stone
 * @create: 2023-11-28 14:25
 **/
public class ResultStorageAndGainFromLocal implements ResultStorageAndGain {

    private Cache<String, ChannelContext> channelContextMap;

    @Resource
    private CoreProperties coreProperties;

    @PostConstruct
    public void init() {
        // 获取配置内容
        ResultStorage resultStorage = coreProperties.getResultStorage();
        ResultStorage.LocalProperties localProperties = resultStorage.getLocalProperties();
        // 装配 channelContextMap
        channelContextMap = CacheBuilder
                .newBuilder()
                // 默认设置 cache 的初始大小为 100（要合理设置该值）
                .initialCapacity(localProperties.getInitalCapacity() == null
                        ? 100 : localProperties.getInitalCapacity())
                // 默认设置并发数为 5，即同一时间最多只有 5 个线程可以向cache中写入
                .concurrencyLevel(localProperties.getConcurrencyLevel() == null
                        ? 5 : localProperties.getConcurrencyLevel())
                // 默认写入 1 分钟后过期
                .expireAfterWrite(localProperties.getExpireAfterWriteMinutes() == null
                        ? 1 : localProperties.getExpireAfterWriteMinutes(), TimeUnit.MINUTES)
                // 构建 cache 实例
                .build();
    }


    @Override
    public ChannelContext getResult(String callId) {
        return channelContextMap.getIfPresent(callId);
    }

    @Override
    public void invalidResult(String callId) {
        channelContextMap.invalidate(callId);
    }

    @Override
    public void storageResult(String callId, ChannelContext data) {
        channelContextMap.put(callId, data);
    }
}