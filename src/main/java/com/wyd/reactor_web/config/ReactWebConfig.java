package com.wyd.reactor_web.config;

import com.wyd.reactor_web.mvc.invoke.NettyMyMethodInvokeHandler;
import com.wyd.reactor_web.mvc.invoke.argument.binder.SpringMyWebDataBinderFactory;
import com.wyd.reactor_web.mvc.invoke.argument.resolver.MyHandlerMethodArgumentResolverComposite;
import com.wyd.reactor_web.mvc.invoke.argument.resolver.self.MyRequestBodyArgumentResolver;
import com.wyd.reactor_web.mvc.invoke.argument.resolver.self.MyRequestParameterArgumentResolver;
import com.wyd.reactor_web.mvc.invoke.interfaces.MyHandlerMethodArgumentResolver;
import com.wyd.reactor_web.mvc.invoke.interfaces.MyWebDataBinderFactory;
import com.wyd.reactor_web.mvc.invoke.processor.SpringInvokePostProcessorContainer;
import com.wyd.reactor_web.mvc.mhandler.SpringMyMethodHandlerFactory;
import com.wyd.reactor_web.mvc.mhandler.assist.DelayAopOrderBeanFactoryPostProcessor;
import com.wyd.reactor_web.mvc.mhandler.SpringMyMethodParameterFactory;
import com.wyd.reactor_web.mvc.mhandler.assist.MyMethodInvokeGearFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @program: reactor_web
 * @description: 考虑到一些内容后期要封装成 spring-boot-starter 项目使用，此处使用一个配置类作为 starter 入口
 * @author: Stone
 * @create: 2023-11-13 18:19
 **/
@Import(ServerRunConfig.class)
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
    public SpringMyMethodParameterFactory myMethodParameterHandlerFactory() {
        return new SpringMyMethodParameterFactory();
    }

    /**
    * @Description: 方法调用需要的 MyMethodInvokeGear 获取的工厂类
    * @Author: Stone
    * @Date: 2023/11/15
    */
    @Bean
    public MyMethodInvokeGearFactory myMethodInvokeGearFactory(SpringMyMethodHandlerFactory springMyMethodHandlerFactory,
                                                               SpringMyMethodParameterFactory myMethodParameterHandlerFactory) {
        return new MyMethodInvokeGearFactory(springMyMethodHandlerFactory, myMethodParameterHandlerFactory);
    }

    @Bean
    public MyHandlerMethodArgumentResolver argumentResolver() {
        MyHandlerMethodArgumentResolverComposite resolverComposite = new MyHandlerMethodArgumentResolverComposite();
        resolverComposite.addMyHandlerMethodArgumentResolver(new MyRequestBodyArgumentResolver());
        resolverComposite.addMyHandlerMethodArgumentResolver(new MyRequestParameterArgumentResolver());
        return resolverComposite;
    }

    @Bean
    public MyWebDataBinderFactory binderFactory() {
        return new SpringMyWebDataBinderFactory();
    }

    @Bean
    public SpringInvokePostProcessorContainer springInvokePostProcessorContainer() {
        return new SpringInvokePostProcessorContainer();
    }

    /**
    * @Description: netty handler 使用的方法调用类，类似于 springMVC 中 dispatcherServlet 的 service() 方法
    * @Author: Stone
    * @Date: 2023/11/15
    */
    @Bean
    public NettyMyMethodInvokeHandler nettyMyMethodInvokeHandler(MyMethodInvokeGearFactory myMethodInvokeGearFactory,
                                                                 MyHandlerMethodArgumentResolver argumentResolver,
                                                                 MyWebDataBinderFactory binderFactory,
                                                                 SpringInvokePostProcessorContainer springInvokePostProcessorContainer
                                                                 ) {
        return new NettyMyMethodInvokeHandler(myMethodInvokeGearFactory, argumentResolver,
                binderFactory, springInvokePostProcessorContainer);
    }

}
