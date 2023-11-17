package com.wyd.reactor_web.config;

import com.wyd.reactor_web.autil.ApplicationContextUtil;
import com.wyd.reactor_web.config.entity.ServerConfig;
import com.wyd.reactor_web.mvc.server.NettyServer;
import com.wyd.reactor_web.mvc.server.handler.HttpServerHandlerInitial;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: reactor_web
 * @description: 服务运行相关配置类
 * @author: Stone
 * @create: 2023-11-17 18:20
 **/
@Configuration
public class ServerRunConfig {

    @Bean
    public ServerConfig serverConfig() {
        return new ServerConfig();
    }

    @Bean
    public HttpServerHandlerInitial httpServerHandlerInitial() {
        return new HttpServerHandlerInitial();
    }

    @Bean
    public NettyServer nettyServer() {
        return new NettyServer();
    }

    @Bean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }
}
