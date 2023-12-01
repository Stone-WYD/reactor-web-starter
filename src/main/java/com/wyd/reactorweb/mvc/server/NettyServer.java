package com.wyd.reactorweb.mvc.server;

import com.wyd.reactorweb.config.property.ServerProperties;
import com.wyd.reactorweb.mvc.server.handler.HttpServerBusinessHandlerInitial;
import com.wyd.reactorweb.mvc.server.handler.HttpServerHandlerInitial;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.WebServerFactory;

/**
 * @program: spring-wyd
 * @description: netty 服务器
 * @author: Stone
 * @create: 2023-10-18 15:04
 **/
@Slf4j
public class NettyServer implements WebServerFactory {

    private final ServerProperties serverProperties;

    private final HttpServerHandlerInitial httpServerHandlerInitial;

    private final HttpServerBusinessHandlerInitial businessHandlerInitial;

    public NettyServer(ServerProperties serverProperties, HttpServerHandlerInitial httpServerHandlerInitial, HttpServerBusinessHandlerInitial businessHandlerInitial) {
        this.serverProperties = serverProperties;
        this.httpServerHandlerInitial = httpServerHandlerInitial;
        this.businessHandlerInitial = businessHandlerInitial;
    }

    public void start(){
        int bossNum = serverProperties.getBossLoopGroupThreadNum() == null ?
                0 : serverProperties.getBossLoopGroupThreadNum();
        int workerNum = serverProperties.getWorkerLoopGroupThreadNum() == null ?
                0 : serverProperties.getWorkerLoopGroupThreadNum();

        NioEventLoopGroup boss = new NioEventLoopGroup(bossNum);
        NioEventLoopGroup worker = new NioEventLoopGroup(workerNum);

        try{
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                            channel.pipeline().addLast(new LoggingHandler());
                            channel.pipeline().addLast(httpServerHandlerInitial);
                            channel.pipeline().addLast(businessHandlerInitial);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(serverProperties.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        }catch ( Exception e){
            log.error("Server error:" , e);
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
