package com.wyd.reactorweb.design.reactor.factory.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wyd.reactorweb.config.property.CoreProperties;
import com.wyd.reactorweb.config.property.core.ResultStorageProperties;
import com.wyd.reactorweb.design.reactor.core.ChannelContext;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @program: reactor-web-starter
 * @description: 使用本地存储 channel 调用结果，不使用泛型，因为要存储所有 channel 中间结果
 * @author: Stone
 * @create: 2023-11-28 14:25
 **/
public class ResultStorageAndGainFromLocal implements ResultStorageAndGain {

    private Cache<String, ChannelContext> channelContextMap;

    private CoreProperties coreProperties;

    public ResultStorageAndGainFromLocal(CoreProperties coreProperties) {
        this.coreProperties = coreProperties;
    }

    @PostConstruct
    public void init() {
        // 没有配置相关内容，按默认配置
        if (coreProperties.getResultStorage() == null || coreProperties.getResultStorage().getLocalProperties() == null) {
            channelContextMap = CacheBuilder
                    .newBuilder()
                    // 设置 cache 的初始大小为 100（要合理设置该值）
                    .initialCapacity(100)
                    // 设置并发数为5，即同一时间最多只有5个线程可以向cache中写入
                    .concurrencyLevel(5)
                    // 写入 10 分钟后过期
                    .expireAfterWrite(10, TimeUnit.MINUTES)
                    // 构建 cache 实例
                    .build();
            return;
        }
        // 获取配置内容
        ResultStorageProperties.LocalProperties localProperties = coreProperties.getResultStorage().getLocalProperties();
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
