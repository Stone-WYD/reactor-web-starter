package com.wyd.reactorweb.design.reactor.core;


import com.wyd.reactorweb.design.reactor.pipeline.PipeLine;
import com.wyd.reactorweb.design.reactor.pipeline.event.BaseEvent;
import com.wyd.reactorweb.design.reactor.pipeline.event.PrepareEvent;
import com.wyd.reactorweb.design.reactor.pipeline.event.RemoteRequestEvent;
import com.wyd.reactorweb.design.reactor.pipeline.event.ResultRenderEvent;
import com.wyd.reactorweb.design.reactor.worker.AppWorker;
import com.wyd.reactorweb.design.reactor.worker.NetWorker;

/**
 * @program: TMSP
 * @description: 事件分发器
 * @author: Stone
 * @create: 2023-07-30 18:25
 **/
public class EventDispatcher {

    private AppWorker appWorker;

    private NetWorker netWorker;

    public void setAppWorker(AppWorker appWorker) {
        this.appWorker = appWorker;
    }

    public void setNetWorker(NetWorker netWorker) {
        this.netWorker = netWorker;
    }

    // 预处理通道
    private final PipeLine preparePipeLine = new PipeLine();

    // 远程请求通道
    private final PipeLine remoteRequestPipeLine = new PipeLine();

    // 结果渲染通道
    private final PipeLine resultRenderPipeLine = new PipeLine();

    public EventDispatcher(AppWorker appWorker, NetWorker netWorker) {
        this.appWorker = appWorker;
        this.netWorker = netWorker;
    }

    public EventDispatcher() {
    }

    public PipeLine getPreparePipeLine() {
        return preparePipeLine;
    }

    public PipeLine getRemoteRequestPipeLine() {
        return remoteRequestPipeLine;
    }

    public PipeLine getResultRenderPipeLine() {
        return resultRenderPipeLine;
    }



    private void walkPipeLine(PipeLine pipeLine, BaseEvent baseEvent){
        // 驱动管道开始工作
        pipeLine.getHandlerList().forEach(handler -> handler.handle(baseEvent.getChannelContext()));
        // 默认handler最后工作
        if (pipeLine.getDefaultHandler() != null) {
            pipeLine.getDefaultHandler().handle(baseEvent.getChannelContext());
        }
    }

    // TODO: 2023/7/31 扩展性设计
    public void dispatch(BaseEvent baseEvent){
        if (baseEvent instanceof PrepareEvent) {
            // 应用线程分发
            appWorker.subTask(()-> walkPipeLine(preparePipeLine, baseEvent));
        }else if (baseEvent instanceof ResultRenderEvent) {
            // 应用线程分发
            appWorker.subTask(()-> walkPipeLine(resultRenderPipeLine, baseEvent));
        }else if (baseEvent instanceof RemoteRequestEvent) {
            // 结果线程分发
            netWorker.subTask(()-> walkPipeLine(remoteRequestPipeLine, baseEvent));
        }
    }

}
