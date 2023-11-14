package com.wyd.reactor_web.config;

import com.wyd.reactor_web.mvc.mhandler.SpringMyMethodHandlerFactory;
import com.wyd.reactor_web.mvc.mhandler.assist.DelayAopOrderBeanFactoryPostProcessor;
import com.wyd.reactor_web.mvc.mhandler.SpringMethodParameterHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: reactor_web
 * @description: 考虑到一些内容后期要封装成 spring-boot-starter 项目使用，此处使用一个配置类作为 starter 入口
 * @author: Stone
 * @create: 2023-11-13 18:19
 **/
@Configuration
public class ReactWebConfig {


    @Bean
    public DelayAopOrderBeanFactoryPostProcessor delayAopOrderBeanFactoryPostProcessor() {
        return new DelayAopOrderBeanFactoryPostProcessor();
    }

    @Bean
    public SpringMyMethodHandlerFactory springMyMethodHandlerFactory() {
        return new SpringMyMethodHandlerFactory();
    }

    @Bean
    public SpringMethodParameterHandlerFactory myMethodParameterHandlerFactory() {
        return new SpringMethodParameterHandlerFactory();
    }
}
