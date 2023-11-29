package com.wyd.reactorweb.config.other;

import com.wyd.reactorweb.config.property.CoreProperties;
import com.wyd.reactorweb.design.reactor.factory.SpringAsynRemoteChannelBuilderFactory;
import com.wyd.reactorweb.design.reactor.factory.SpringAsynRemoteChannelInitializationFactory;
import com.wyd.reactorweb.design.reactor.factory.component.ResultStorageAndGain;
import com.wyd.reactorweb.design.reactor.factory.component.ResultStorageAndGainFromLocal;
import com.wyd.reactorweb.design.reactor.worker.AppWorker;
import com.wyd.reactorweb.design.reactor.worker.NetWorker;
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

    @Bean
    public CoreProperties coreProperties() {
        return new CoreProperties();
    }

    // 注入 netWorker 和 appWorker 线程
    @Bean
    public NetWorker netWorker(CoreProperties coreProperties) {
        return new NetWorker(coreProperties.getNetWorker());
    }
    @Bean
    public AppWorker appWorker(CoreProperties coreProperties) {
        return new AppWorker(coreProperties.getAppWorker());
    }

    // 默认创建
    @Bean
    // TODO: 2023/11/28 如此使用有待确认
    /*@ConditionalOnProperty(name = "myreact.core.result-storage.type",
            havingValue = "${default.myreact.core.result-storage-type}", matchIfMissing = true)*/
    public ResultStorageAndGain defaultResultStorageAndGain() {
        return new ResultStorageAndGainFromLocal();
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
