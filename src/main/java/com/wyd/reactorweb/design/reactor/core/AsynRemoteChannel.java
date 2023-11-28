package com.wyd.reactorweb.design.reactor.core;

import cn.hutool.core.collection.CollectionUtil;
import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.design.reactor.factory.ResultStorageAndGain;
import com.wyd.reactorweb.design.reactor.pipeline.Handler;
import com.wyd.reactorweb.design.reactor.pipeline.event.PrepareEvent;
import com.wyd.reactorweb.design.reactor.pipeline.event.RemoteRequestEvent;
import com.wyd.reactorweb.design.reactor.pipeline.event.ResultRenderEvent;
import com.wyd.reactorweb.design.reactor.worker.AppWorker;
import com.wyd.reactorweb.design.reactor.worker.NetWorker;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @program: TMSP
 * @description:
 * @author: Stone
 * @create: 2023-07-31 10:13
 **/
@Slf4j
public class AsynRemoteChannel<T> {

    private final EventDispatcher eventDispatcher = new EventDispatcher();
    private AsynRemoteServiceProxy<T> serviceProxy;

    // 是否启动
    private boolean start = false;

    private ResultStorageAndGain resultStorageAndGain;

    public AsynRemoteChannel(AsynRemoteServiceProxy<T> serviceProxy, ResultStorageAndGain resultStorageAndGain) {
        this.serviceProxy = serviceProxy;
        this.resultStorageAndGain = resultStorageAndGain;
    }

    private void startReact() {
        // 疲劳处理
        while(true){
            // 1.被调用方在没有返回时应该等待结果的产生，从而使得此处的方法阻塞在此。
            // 2.serviceProxy 建议使用 dubbo 等 rpc 框架进行方法调用，从而使得此方法阻塞时不会占用线程资源
            AsynReceptResult<Map<String, AjaxResult<T>>> asynReceptResult =
                    serviceProxy.requestReceipt();
            // 未能获取到结果
            if (!asynReceptResult.isSuccess()){
                log.error(asynReceptResult.getErrMsg());
                continue;
            }
            // 结果中没有数据
            if (CollectionUtil.isEmpty(asynReceptResult.getData())) {
                continue;
            }
            // 对结果中的数据进行处理
            for (Map.Entry<String, AjaxResult<T>> entry : asynReceptResult.getData().entrySet()) {
                String callId = entry.getKey();
                ChannelContext<T> storeContext = resultStorageAndGain.getResult(callId);
                if (storeContext == null){
                    // 失去有效性，忽略
                    continue;
                }
                resultStorageAndGain.invalidResult(callId);
                storeContext.setAsynReceptResult(entry.getValue());
                // 发布结果解析事件，交给 工作线程 的处理器处理
                eventDispatcher.dispatch(new ResultRenderEvent(storeContext));
            }
        }
    }

    public void start() {
        if (start){
            return;
        }

        // 1.预备通道连接远程请求
        eventDispatcher.getPreparePipeLine().setDefaultHandler( channelContext -> {
            // 预备通道的兜底逻辑是抛出远程调用事件，进行逻辑串联
            eventDispatcher.dispatch(new RemoteRequestEvent(channelContext));
        });
        // 2.远程请求调用，并生成调用结果任务（生产者角色）
        eventDispatcher.getRemoteRequestPipeLine().setDefaultHandler(
                channelContext -> {
                    // 调用远程服务
                    AjaxResult<String> rpcResult = serviceProxy.call(channelContext);
                    //此时远程服务不会返回计算结果，而是先异步返回了此次调用的callId，在后面要获取真正结果时使用 callId 从批量结果中获取真正的结果放入到channelContext中
                    channelContext.setCallId(rpcResult.getData());
                    resultStorageAndGain.storageResult(rpcResult.getData(), channelContext);
                }
        );
        Thread eventloop = new Thread(this::startReact);
        eventloop.start();
        start = true;
    }

    public AsynRemoteChannel(NetWorker netWorker, AppWorker appWorker) {
        eventDispatcher.setNetWorker(netWorker);
        eventDispatcher.setAppWorker(appWorker);
    }

    public void addPrepareHandler(Handler handler) {
        eventDispatcher.getPreparePipeLine().getHandlerList().add(handler);
    }

    public void addResultRenderHandler(Handler handler){
        eventDispatcher.getResultRenderPipeLine().getHandlerList().add(handler);
    }

    public void bindRemoteService(AsynRemoteServiceProxy<T> asynRemoteServiceProxy) {
        serviceProxy = asynRemoteServiceProxy;
    }

    /**
    * @Description: channel 的驱动方法
    * @Author: Stone
    * @Date: 2023/11/27
    */
    public void walk(ChannelContext channelContext){
        eventDispatcher.dispatch(new PrepareEvent(channelContext));
    }
}
