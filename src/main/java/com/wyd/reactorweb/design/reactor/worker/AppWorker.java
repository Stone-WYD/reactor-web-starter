package com.wyd.reactorweb.design.reactor.worker;

import com.wyd.reactorweb.config.property.core.WorkerProperties;
import lombok.extern.slf4j.Slf4j;

import static com.wyd.reactorweb.constant.CoreConstants.APPWORKER_NAME_FORMAT;

/**
 * @program: TMSP
 * @description:
 * @author: Stone
 * @create: 2023-07-30 18:26
 **/
@Slf4j
public class AppWorker extends Worker {


    public AppWorker(WorkerProperties workerProperties) {
        super(workerProperties);
    }

    public void subTask(Runnable runnable) {
        executor.submit(runnable);
    }

    @Override
    public String getThreadFactoryNameFormat() {
        return APPWORKER_NAME_FORMAT;
    }
}
