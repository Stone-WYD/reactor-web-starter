package com.wyd.reactorweb.mvc.invoke;

import com.wyd.reactorweb.mvc.mhandler.entity.MyMethodParameter;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: reactor_web
 * @description: 后处理上下文
 * @author: Stone
 * @create: 2023-11-16 13:56
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvokePostPrcessorContext {

    // 调用方法的传参类型
    private MyMethodParameter[] myMethodParameters;

    // 调用方法的传参实例
    private Object[] parameters;

    // 调用方法的返回类型
    private Class<?> returnClass;

    // 调用后的返回结果
    private Object invokeResult;

    // netty 调用过程 pipeline 中当前 handler 对应的 ChannelHandlerContext 实例
    private ChannelHandlerContext channelHandlerContext;
}
