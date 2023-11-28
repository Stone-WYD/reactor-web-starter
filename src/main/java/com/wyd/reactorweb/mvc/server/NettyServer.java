package com.wyd.reactorweb.mvc.server;

import com.wyd.reactorweb.config.property.ServerProperties;
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

import javax.annotation.Resource;

/**
 * @program: spring-wyd
 * @description: netty 服务器
 * @author: Stone
 * @create: 2023-10-18 15:04
 **/
@Slf4j
public class NettyServer implements WebServerFactory {

    @Resource
    private ServerProperties serverProperties;

    @Resource
    private HttpServerHandlerInitial httpServerHandlerInitial;

    public void start(){
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
