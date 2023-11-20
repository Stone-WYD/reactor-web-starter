package com.wyd.reactorweb.mvc.listeners;


import com.wyd.reactorweb.mvc.server.NettyServer;
import com.wyd.reactorweb.util.ApplicationContextUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @program: reactor-web-starter
 * @description: 等到 bean 单例都初始化完成时再启动 netty 服务
 * @author: Stone
 * @create: 2023-11-20 15:14
 **/
public class MyNettyStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        NettyServer nettyServer = ApplicationContextUtil.getBeanOfType(NettyServer.class);
        new Thread(() -> nettyServer.start()).start();
    }
}
