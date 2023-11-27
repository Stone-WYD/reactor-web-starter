package com.wyd.reactorweb.test.reactor.demo.configuration;

import cn.hutool.json.JSONUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.design.reactor.core.AsynReceptResult;
import com.wyd.reactorweb.design.reactor.core.AsynRemoteChannel;
import com.wyd.reactorweb.design.reactor.core.AsynRemoteServiceProxy;
import com.wyd.reactorweb.design.reactor.core.ChannelContext;
import com.wyd.reactorweb.test.reactor.demo.SendInfo;
import com.wyd.reactorweb.test.reactor.demo.service.RemoteMessageSendService;
import com.wyd.reactorweb.design.reactor.worker.AppWorker;
import com.wyd.reactorweb.design.reactor.worker.NetWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: TMSP
 * @description: reactor设计模式中用到的配置类，进行一些bean的配置。当前配置类只是个 demo，实际使用时可以根据该 demo 进行配置
 * @author: Stone
 * @create: 2023-07-31 10:09
 **/
@Configuration
public class ReactorDesignDemoConfig {

    /**
    * @Description: 缓存结果，需要结果的时候可以在此处获取
    * @Author: Stone
    * @Date: 2023/11/27
    */
    public final static Cache<String, Boolean> localCache = CacheBuilder
            .newBuilder()
            // 设置 cache 的初始大小为 100（要合理设置该值）
            .initialCapacity(100)
            // 设置并发数为5，即同一时间最多只有5个线程可以向cache中写入
            .concurrencyLevel(5)
            // 写入 1 分钟后过期
            .expireAfterWrite(1, TimeUnit.MINUTES)
            // 构建 cache 实例
            .build();

    @Bean
    public NetWorker netWorker() {
        return new NetWorker();
    }

    @Bean
    public AppWorker appWorker() {
        return new AppWorker();
    }



    /**
    * @Description: 多个 channel 可以共用同样的一组 netWorker、appWorker
    * @Author: Stone
    * @Date: 2023/11/27
    */
    @Bean
    public AsynRemoteChannel remoteMessageChannel(RemoteMessageSendService remoteMessageSendService,
                                                  NetWorker netWorker, AppWorker appWorker){
        AsynRemoteChannel<Boolean> asynRemoteChannel = new AsynRemoteChannel<>(netWorker, appWorker);

        // 构建短信发送服务
        AsynRemoteServiceProxy<Boolean> asynRemoteServiceProxy = buildAsynRemoteServiceProxy(remoteMessageSendService);
        asynRemoteChannel.bindRemoteService(asynRemoteServiceProxy);

        // 前置处理中的远程调用参数打印
        asynRemoteChannel.addPrepareHandler(
                // 此处只是展示 demo
                channelContext -> System.out.println(JSONUtil.toJsonStr(channelContext.getParamMap()))
        );

        // 后置处理中的远程调用结果打印
        asynRemoteChannel.addResultRenderHandler(
                // 此处只是展示 demo
                channelContext -> System.out.println(JSONUtil.toJsonStr(channelContext.getAsynReceptResult()))
        );

        // 后置处理中的将结果放到队列中
        asynRemoteChannel.addResultRenderHandler(
                // TODO: 2023/8/2 这里做演示用，实际情况如果不是单体的，则要用集群缓存
                channelContext -> {
                    // TODO: 2023/10/17  缓存在代码中并没有被使用到，在当前示例中只用作展示。
                    //  具体如果有需要，localCache 的 key 可以不是 callId，而是根据实际传参而定。如果这样做，在调用方法前，就可以先看是否命中缓存来减小服务器压力
                    localCache.put(channelContext.getCallId(), (Boolean) channelContext.getAsynReceptResult().getData());
                }
        );

        // 启动通道反应堆
        asynRemoteChannel.start();
        return asynRemoteChannel;
    }

    private static AsynRemoteServiceProxy<Boolean> buildAsynRemoteServiceProxy
            (RemoteMessageSendService remoteMessageSendService){
        return new AsynRemoteServiceProxy<Boolean>() {
            @Override
            public AjaxResult<String> call(ChannelContext channelContext) {
                Map paramMap = channelContext.getParamMap();
                SendInfo sendInfo = (SendInfo) paramMap.get("sendInfo");
                return remoteMessageSendService.send(sendInfo);
            }

            @Override
            public AsynReceptResult<Map<String, AjaxResult<Boolean>>> requestReceipt() {
                return remoteMessageSendService.getResultList();
            }
        };
    }

}
