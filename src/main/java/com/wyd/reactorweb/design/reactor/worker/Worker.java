package com.wyd.reactorweb.design.reactor.worker;

import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wyd.reactorweb.config.property.CoreProperties;
import com.wyd.reactorweb.util.MyThreadPoolExecutor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: TMSP
 * @description: 线程类
 * @author: Stone
 * @create: 2023-07-31 11:48
 **/
public abstract class Worker {

    protected final ThreadPoolExecutor executor;

    public Worker(CoreProperties.WorkerProperties workerProperties) {

        String nameformat = StrUtil.isBlank(workerProperties.getThreadNamePrefix()) ?
                getThreadFactoryNameFormat() : workerProperties.getThreadNamePrefix() + "-%d";
        // 给线程命名
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(nameformat).build();
        // 默认通过核心数确定线程数
        int processors = workerProperties.getCorePoolSize() == null ?
                Runtime.getRuntime().availableProcessors() : workerProperties.getCorePoolSize();
        int maximumPoolSize = workerProperties.getMaximumPoolSize() == null ?
                processors * 2 : workerProperties.getMaximumPoolSize();
        int keepLiveMinutes = workerProperties.getKeepLiveMinutes() == null ?
                0 : workerProperties.getKeepLiveMinutes();
        // 可配置内容暂定如此
        executor = new MyThreadPoolExecutor(processors,
                maximumPoolSize,
                keepLiveMinutes,
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public abstract void subTask(Runnable runnable);

    protected abstract String getThreadFactoryNameFormat();

}
