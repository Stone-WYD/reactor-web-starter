package com.wyd.reactorweb.design.reactor.worker;

import com.wyd.reactorweb.config.property.CoreProperties;
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


    public AppWorker(CoreProperties.WorkerProperties workerProperties) {
        super(workerProperties);
    }

    public void subTask(Runnable runnable) {
        appExecutor.submit(runnable);
    }

    @Override
    public String getThreadFactoryNameFormat() {
        return APPWORKER_NAME_FORMAT;
    }
}
