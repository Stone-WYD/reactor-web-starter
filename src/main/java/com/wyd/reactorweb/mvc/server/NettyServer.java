package com.wyd.reactorweb.mvc.server;

import com.wyd.reactorweb.config.property.ServerProperties;
import com.wyd.reactorweb.mvc.server.handler.ResultHandler;
import com.wyd.reactorweb.mvc.server.handler.business.HttpServerBusinessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
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

    private final ServerProperties serverProperties;

    @Resource
    private ResultHandler resultHandler;

    @Resource
    private HttpServerBusinessHandler businessHandler;

    public NettyServer(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    public void start(){
        int bossNum = serverProperties.getBossLoopGroupThreadNum() == null ?
                0 : serverProperties.getBossLoopGroupThreadNum();
        int workerNum = serverProperties.getWorkerLoopGroupThreadNum() == null ?
                0 : serverProperties.getWorkerLoopGroupThreadNum();
        int businessNum = serverProperties.getBusinessThreadNum() == null ? 1 : serverProperties.getBusinessThreadNum();

        NioEventLoopGroup boss = new NioEventLoopGroup(bossNum);
        NioEventLoopGroup worker = new NioEventLoopGroup(workerNum);
        final EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(businessNum);

        try{
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            // pipeline.addLast(new LoggingHandler());
                            // http请求编解码器,请求解码，响应编码
                            pipeline.addLast(businessGroup, "serverCodec", new HttpServerCodec());
                            // http请求报文聚合为完整报文，最大请求报文为10M
                            pipeline.addLast(businessGroup, "aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
                            // 响应报文压缩 会比较影响并发性能
                            // pipeline.addLast(businessGroup, "compress", new HttpContentCompressor());
                            pipeline.addLast(businessGroup, "resultHandler", resultHandler);
                            pipeline.addLast(businessGroup, "businessHandler", businessHandler);
                        }
                    })
                    // 临时存放已完成三次握手的请求的队列的最大长度。
                    // 如果未设置或所设置的值小于1，Java将使用默认值50。
                    // 如果大于队列的最大长度，请求会被拒绝
                    .option(ChannelOption.SO_BACKLOG,2000)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
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
