package com.wyd.reactorweb.design.reactor.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.wyd.reactorweb.annotation.ChannelInfo;
import com.wyd.reactorweb.design.reactor.core.AsynRemoteServiceProxy;
import com.wyd.reactorweb.design.reactor.pipeline.Handler;
import com.wyd.reactorweb.util.ApplicationContextUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: reactor-web-starter
 * @description: AsynRemoteChannel 工厂
 * @author: Stone
 * @create: 2023-11-28 09:42
 **/
public class SpringAsynRemoteChannelInitializationFactory implements InstantiationAwareBeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private SpringAsynRemoteChannelBuilderFactory builderFactory;

    private final List<ChannelClassTypeInfo> channelClassTypeInfoList = new ArrayList<>();

    /**
    * @Description: 实例化单例之前进行的操作，获取类上的注解信息使用
    * @Author: Stone
    * @Date: 2023/11/28
    */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {


        ChannelInfo channelInfo = AnnotationUtils.findAnnotation(beanClass, ChannelInfo.class);
        if (channelInfo == null) return null;

        // 检查注解是否有问题
        if (!AsynRemoteServiceProxy.class.isAssignableFrom(beanClass))
            throw new RuntimeException("@ChannelInfo 注解请加在 AsynRemoteServiceProxy 的子类上");

        // 获取类型信息存储起来
        ChannelClassTypeInfo channelClassTypeInfo = new ChannelClassTypeInfo();
        channelClassTypeInfo.setBeanName(beanName);
        channelClassTypeInfo.setServiceClassType((Class<? extends AsynRemoteServiceProxy>) beanClass);
        Class<? extends Handler> preparedType = channelInfo.prepareType();
        channelClassTypeInfo.setPrepareClassType(preparedType == Handler.class ? null : preparedType);
        Class<? extends Handler> renderType = channelInfo.renderType();
        channelClassTypeInfo.setRenderClassType(renderType == Handler.class ? null : renderType);
        channelClassTypeInfoList.add(channelClassTypeInfo);
        return null;
    }

    /**
    * @Description: 所有实例都初始化后，找到合适的 bean，构建对应的 channel
    * @Author: Stone
    * @Date: 2023/11/28
    */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (CollectionUtil.isNotEmpty(channelClassTypeInfoList)) {
            for (ChannelClassTypeInfo classInfo : channelClassTypeInfoList) {
                // 1. 获取 service
                Class<? extends AsynRemoteServiceProxy> serviceClassType = classInfo.getServiceClassType();
                AsynRemoteServiceProxy asynRemoteServiceProxy = ApplicationContextUtil.getBeanOfType(serviceClassType);
                // 2. 获取 prepare handler 列表
                Class<? extends Handler> prepareClassType = classInfo.getPrepareClassType();
                List<? extends Handler> prepareHandlerList = new ArrayList<>();
                if (prepareClassType != null) {
                    prepareHandlerList = ApplicationContextUtil.getBeansOfType(prepareClassType);
                }
                // 3. 获取 render handler 列表
                Class<? extends Handler> renderClassType = classInfo.getRenderClassType();
                List<? extends Handler> renderHandlerList = new ArrayList<>();
                if (renderClassType != null) {
                    renderHandlerList = ApplicationContextUtil.getBeansOfType(renderClassType);
                }
                // 4. 创建新的 channel
                builderFactory.build(classInfo.getBeanName(), asynRemoteServiceProxy,
                        (List<Handler>) prepareHandlerList, (List<Handler>) renderHandlerList);
            }
        }
    }

    private static class ChannelClassTypeInfo{

        private String beanName;

        private Class<? extends AsynRemoteServiceProxy> serviceClassType;

        private Class<? extends Handler> prepareClassType;

        private Class<? extends Handler> renderClassType;

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public Class<? extends AsynRemoteServiceProxy> getServiceClassType() {
            return serviceClassType;
        }

        public void setServiceClassType(Class<? extends AsynRemoteServiceProxy> serviceClassType) {
            this.serviceClassType = serviceClassType;
        }

        public Class<? extends Handler> getPrepareClassType() {
            return prepareClassType;
        }

        public void setPrepareClassType(Class<? extends Handler> prepareClassType) {
            this.prepareClassType = prepareClassType;
        }

        public Class<? extends Handler> getRenderClassType() {
            return renderClassType;
        }

        public void setRenderClassType(Class<? extends Handler> renderClassType) {
            this.renderClassType = renderClassType;
        }
    }
}
