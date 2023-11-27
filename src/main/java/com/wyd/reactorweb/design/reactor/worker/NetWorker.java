package com.wyd.reactorweb.design.reactor.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wyd.reactorweb.util.MyThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: TMSP
 * @description:
 * @author: Stone
 * @create: 2023-07-30 18:26
 **/
@Slf4j
public class NetWorker extends Worker{
    private static final ThreadPoolExecutor netExecutor;

    static{
        // 给线程命名
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("NetWorker-worker-%d").build();
        // 默认通过核心数确定线程数
        // TODO: 2023/11/27 后面改为可配置
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("NetWorker:processors:{}", processors);
        netExecutor = new MyThreadPoolExecutor(processors,
                processors * 2,
                0L,
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(1000),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public void subTask(Runnable runnable) {
        netExecutor.submit(runnable);
    }
}
