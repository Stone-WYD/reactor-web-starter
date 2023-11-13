package com.wyd.reactor_web.mvc.mhandler.assist;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @program: spring-wyd
 * @description: 延后 aop 后处理器工厂执行顺序
 * @author: Stone
 * @create: 2023-11-13 17:31
 **/
public class DelayAopOrderBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        AnnotationAwareAspectJAutoProxyCreator aopBean = beanFactory.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        aopBean.setOrder(Integer.MIN_VALUE + 1);
    }

}
