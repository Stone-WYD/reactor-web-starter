package com.wyd.reactorweb.mvc.invoke.processor.impl;

import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.mvc.invoke.InvokePostPrcessorContext;
import com.wyd.reactorweb.mvc.invoke.processor.InvokePostProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @program: reactor-web-starter
 * @description: 处理返回结果是 AjaxResult 的 controller 方法
 * @author: Stone
 * @create: 2023-12-01 13:59
 **/
public class AjaxResultHandleInvokePostProcessor implements InvokePostProcessor {

    @Override
    public boolean support(InvokePostPrcessorContext postContext) {
        return postContext.getReturnClass() == AjaxResult.class;
    }

    @Override
    public void handleAfter(InvokePostPrcessorContext postContext) {
        ChannelHandlerContext ctx = postContext.getChannelHandlerContext();
        ctx.write(postContext.getInvokeResult());
    }
}
