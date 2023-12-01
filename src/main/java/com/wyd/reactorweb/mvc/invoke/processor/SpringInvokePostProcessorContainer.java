package com.wyd.reactorweb.mvc.invoke.processor;

import com.wyd.reactorweb.design.postprocessor.BasePostProcessor;
import com.wyd.reactorweb.mvc.invoke.InvokePostPrcessorContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: reactor_web
 * @description: 调用相关后处理器工厂
 * @author: Stone
 * @create: 2023-11-17 17:07
 **/
public class SpringInvokePostProcessorContainer implements BeanPostProcessor {

    private final List<InvokePostProcessor> postProcessors = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof InvokePostProcessor) {
            postProcessors.add((InvokePostProcessor) bean);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    public boolean handleBefore(InvokePostPrcessorContext postContext){
        for (BasePostProcessor<InvokePostPrcessorContext> postProcessor : postProcessors) {
            // 如果支持处理，才会处理
            if (postProcessor.support(postContext)) {
                // 处理后需要停止后续操作则返回 false
                if (!postProcessor.handleBefore(postContext)) {
                    return false;
                }
            }
        }
        return true; // 正常返回 true
    }

    public void handleAfter(InvokePostPrcessorContext postContext){
        for (BasePostProcessor<InvokePostPrcessorContext> postProcessor : postProcessors) {
            // 如果支持处理，才会处理
            if (postProcessor.support(postContext)) {
                postProcessor.handleAfter(postContext);
            }
        }
    }
}
