package com.wyd.reactorweb.mvc.server;

import com.wyd.reactorweb.config.entity.ServerConfig;
import com.wyd.reactorweb.mvc.server.handler.HttpServerHandlerInitial;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.WebServerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @program: spring-wyd
 * @description: netty 服务器
 * @author: Stone
 * @create: 2023-10-18 15:04
 **/
@Slf4j
@EnableConfigurationProperties({ServerConfig.class})
public class NettyServer implements WebServerFactory {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private HttpServerHandlerInitial httpServerHandlerInitial;

    @PostConstruct
    public void init(){
        new Thread(this::start).start();
    }

    private void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                            channel.pipeline().addLast(new LoggingHandler());
                            channel.pipeline().addLast(httpServerHandlerInitial);}
                    });
            ChannelFuture channelFuture = bootstrap.bind(serverConfig.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        }catch ( Exception e){
            log.error("Server error:" , e);
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
