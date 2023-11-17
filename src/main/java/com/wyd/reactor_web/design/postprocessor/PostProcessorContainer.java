package com.wyd.reactor_web.design.postprocessor;

import cn.hutool.core.collection.CollectionUtil;
import com.wyd.reactor_web.autil.ApplicationContextUtil;

import java.util.Comparator;
import java.util.List;

public class PostProcessorContainer<T>{

    private Class<? extends BasePostProcessor<T>> monitorPostProcessorClass;

    private List<? extends BasePostProcessor<T>> postProcessors;

    private volatile boolean hasInit = false;

    private PostProcessorContainer() {
    }

    public static <T> PostProcessorContainer<T> getInstance(Class<? extends BasePostProcessor<T>> monitorPostProcessorClass){
        PostProcessorContainer<T> postProcessorContainer = new PostProcessorContainer<>();
        postProcessorContainer.monitorPostProcessorClass = monitorPostProcessorClass;
        // 使用者无法 new 对象，只能通过该方法获取实例，给方法扩展留空间
        return postProcessorContainer;
    }

    private void initPostProcessors() {
        if (!hasInit) {
            synchronized (this) {
                if (!hasInit) {
                    this.postProcessors
                            = ApplicationContextUtil.getBeansOfType(monitorPostProcessorClass);
                    if (CollectionUtil.isNotEmpty(postProcessors)) {
                        postProcessors.sort(Comparator.comparing((BasePostProcessor<T> o) -> o.getPriprity()));
                    }
                    hasInit = true;
                }
            }
        }

    }

    public boolean handleBefore(T postContext){
        initPostProcessors();
        for (BasePostProcessor<T> postProcessor : postProcessors) {
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

    public void handleAfter(T postContext){
        initPostProcessors();

        for (BasePostProcessor<T> postProcessor : postProcessors) {
            // 如果支持处理，才会处理
            if (postProcessor.support(postContext)) {
                postProcessor.handleAfter(postContext);
            }
        }
    }
}
