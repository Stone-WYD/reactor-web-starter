package com.wyd.reactorweb.config.other;

import com.wyd.reactorweb.mvc.server.handler.business.HttpServerBusinessHandler;
import com.wyd.reactorweb.util.ApplicationContextUtil;
import com.wyd.reactorweb.config.property.ServerProperties;
import com.wyd.reactorweb.mvc.server.NettyServer;
import com.wyd.reactorweb.mvc.server.handler.ResultHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: reactor_web
 * @description: 服务运行相关配置类
 * @author: Stone
 * @create: 2023-11-17 18:20
 **/
@Configuration
@EnableConfigurationProperties({ServerProperties.class})
public class ServerRunConfig {


    @Bean
    public ResultHandler resultHandler() {
        return new ResultHandler();
    }

    @Bean
    public HttpServerBusinessHandler httpServerBusinessHandler() {
        return new HttpServerBusinessHandler();
    }

    @Bean
    public NettyServer nettyServer(ServerProperties serverProperties) {
        return new NettyServer(serverProperties);
    }

    @Bean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }
}
