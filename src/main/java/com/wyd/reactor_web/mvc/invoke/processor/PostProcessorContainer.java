package com.wyd.reactor_web.mvc.invoke.processor;

import cn.hutool.core.collection.CollectionUtil;
import com.wyd.reactor_web.autil.ApplicationContextUtil;

import java.util.Comparator;
import java.util.List;

public class PostProcessorContainer<T>{

    private Class<BasePostProcessor<T>> monitorPostProcessorClass;


    private PostProcessorContainer() {
    }

    public static <T> PostProcessorContainer<T> getInstance(Class<BasePostProcessor<T>> monitorPostProcessorClass){
        PostProcessorContainer<T> postProcessorContainer = new PostProcessorContainer<>();
        postProcessorContainer.monitorPostProcessorClass = monitorPostProcessorClass;
        // 使用者无法 new 对象，只能通过该方法获取实例，给方法扩展留空间
        return postProcessorContainer;
    }

    public boolean handleBefore(PostContext<T> postContext){
        List<? extends BasePostProcessor<T>> postProcessors
                = ApplicationContextUtil.getBeansOfType(monitorPostProcessorClass);
        if (CollectionUtil.isEmpty(postProcessors)) return true; // 没有操作则返回true

        // 优先级越高，执行时越靠近核心
        postProcessors.sort(Comparator.comparing((BasePostProcessor<T> o) -> o.getPriprity()));

        for (BasePostProcessor<T> postProcessor : postProcessors) {
            // 如果支持处理，才会处理
            if (postProcessor.support(postContext)) {
                postProcessor.handleBefore(postContext);
            }
        }
        return false; // 有操作则返回false
    }

    public void handleAfter(PostContext<T> postContext){
        List<? extends BasePostProcessor<T>> postProcessors
                = ApplicationContextUtil.getBeansOfType(monitorPostProcessorClass);
        if (CollectionUtil.isEmpty(postProcessors)) return ;

        // 优先级越高，执行时越靠近核心
        postProcessors.sort(Comparator.comparing((BasePostProcessor<T> o) -> o.getPriprity()).reversed());

        for (BasePostProcessor<T> postProcessor : postProcessors) {
            // 如果支持处理，才会处理
            if (postProcessor.support(postContext)) {
                postProcessor.handleAfter(postContext);
            }
        }
    }
}
