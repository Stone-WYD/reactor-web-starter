package com.wyd.reactorweb.config.other;

import com.wyd.reactorweb.config.property.CoreProperties;
import com.wyd.reactorweb.design.reactor.factory.SpringAsynRemoteChannelBuilderFactory;
import com.wyd.reactorweb.design.reactor.factory.SpringAsynRemoteChannelInitializationFactory;
import com.wyd.reactorweb.design.reactor.factory.component.ResultStorageAndGain;
import com.wyd.reactorweb.design.reactor.factory.component.ResultStorageAndGainFromLocal;
import com.wyd.reactorweb.design.reactor.worker.AppWorker;
import com.wyd.reactorweb.design.reactor.worker.NetWorker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: reactor-web-starter
 * @description: reactor 核心配置相关内容
 * @author: Stone
 * @create: 2023-11-28 10:24
 **/
@Configuration
@EnableConfigurationProperties({CoreProperties.class})
public class CoreConfig {

    // 注入 netWorker 和 appWorker 线程
    @Bean
    public NetWorker netWorker(CoreProperties coreProperties) {
        if (coreProperties == null || coreProperties.getNetWorker() == null) {
            return new NetWorker(new CoreProperties.WorkerProperties()) ;
        }
        return new NetWorker(coreProperties.getNetWorker());
    }
    @Bean
    public AppWorker appWorker(CoreProperties coreProperties) {
        if (coreProperties == null || coreProperties.getAppWorker() == null) {
            return new AppWorker(new CoreProperties.WorkerProperties());
        }
        return new AppWorker(coreProperties.getAppWorker());
    }

    // 默认创建
    @Bean
    @ConditionalOnProperty(name = "myreact.core.result-storage.type",
            havingValue = "local", matchIfMissing = true)
    public ResultStorageAndGain defaultResultStorageAndGain(CoreProperties coreProperties) {
        return new ResultStorageAndGainFromLocal(coreProperties);
    }

    @Bean
    public SpringAsynRemoteChannelBuilderFactory springAsynRemoteChannelBuilderFactory() {
        return new SpringAsynRemoteChannelBuilderFactory();
    }

    @Bean
    public SpringAsynRemoteChannelInitializationFactory springAsynRemoteChannelInitializationFactory() {
        return new SpringAsynRemoteChannelInitializationFactory();
    }

}
