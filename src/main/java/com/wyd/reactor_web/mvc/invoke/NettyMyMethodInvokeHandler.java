package com.wyd.reactor_web.mvc.invoke;

import com.wyd.reactor_web.mvc.invoke.interfaces.MyMethodInvokeHandler;
import com.wyd.reactor_web.mvc.mhandler.assist.MyMethodInvokeGearFactory;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodHandler;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodInvokeGear;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @program: reactor_web
 * @description: 使用 Spring 和 Netty 完成请求调用的功能
 * @author: Stone
 * @create: 2023-11-15 14:28
 **/
public class NettyMyMethodInvokeHandler implements MyMethodInvokeHandler {

    private final MyMethodInvokeGearFactory myMethodInvokeGearFactory;

    public NettyMyMethodInvokeHandler(MyMethodInvokeGearFactory myMethodInvokeGearFactory) {
        this.myMethodInvokeGearFactory = myMethodInvokeGearFactory;
    }

    @Override
    public void invoke(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        // 请求地址
        String path = httpRequest.uri();
        if (path.indexOf('?') != -1) {
            path = path.substring(0, path.indexOf('?'));
        }


        // 方法调用需要的东西
        MyMethodInvokeGear invokeGear = myMethodInvokeGearFactory.getMyMethodInvokeGearByUrl(path);

        MyMethodHandler myMethodHandler = invokeGear.getMyMethodHandler();

        MyMethodParameter[] parameterArrays = invokeGear.getParameterArrays();
        Object[] parameters = new Object[parameterArrays.length];

        // 组合模式：准备方法调用参数

        // 后处理器方式：方法调用前的操作

        // 方法调用

        // 后处理器方法：方法调用后的操作

    }

}
