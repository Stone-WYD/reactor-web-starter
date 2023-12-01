package com.wyd.reactorweb.config.other;

import com.wyd.reactorweb.mvc.invoke.NettyMyMethodInvokeHandler;
import com.wyd.reactorweb.mvc.invoke.argument.binder.SpringMyWebDataBinderFactory;
import com.wyd.reactorweb.mvc.invoke.argument.resolver.MyHandlerMethodArgumentResolverComposite;
import com.wyd.reactorweb.mvc.invoke.argument.resolver.self.MyRequestBodyArgumentResolver;
import com.wyd.reactorweb.mvc.invoke.argument.resolver.self.MyRequestParameterArgumentResolver;
import com.wyd.reactorweb.mvc.invoke.interfaces.MyHandlerMethodArgumentResolver;
import com.wyd.reactorweb.mvc.invoke.interfaces.MyWebDataBinderFactory;
import com.wyd.reactorweb.mvc.invoke.processor.SpringInvokePostProcessorContainer;
import com.wyd.reactorweb.mvc.invoke.processor.impl.AjaxResultHandleInvokePostProcessor;
import com.wyd.reactorweb.mvc.listeners.MyInitListener;
import com.wyd.reactorweb.mvc.listeners.MyNettyStartListener;
import com.wyd.reactorweb.mvc.mhandler.SpringMyMethodHandlerFactory;
import com.wyd.reactorweb.mvc.mhandler.SpringMyMethodParameterFactory;
import com.wyd.reactorweb.mvc.mhandler.assist.DelayAopOrderBeanFactoryPostProcessor;
import com.wyd.reactorweb.mvc.mhandler.assist.MyMethodInvokeGearFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: reactor-web-starter
 * @description: mvc相关内容配置类
 * @author: Stone
 * @create: 2023-11-28 11:15
 **/
@Configuration
public class MvcConfig {

    /**
     * @Description: 在容器刷新完成后，进行一些内容的初始化
     * @Author: Stone
     * @Date: 2023/11/20
     */
    @Bean
    public MyInitListener myInitListener() {
        return new MyInitListener();
    }

    /**
     * @Description: 等到 bean 单例都初始化完成时再启动 netty 服务
     * @Author: Stone
     * @Date: 2023/11/20
     */
    @Bean
    public MyNettyStartListener myNettyStartListener() {
        return new MyNettyStartListener();
    }

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
    public AjaxResultHandleInvokePostProcessor ajaxResultHandleInvokePostProcessor() {
        return new AjaxResultHandleInvokePostProcessor();
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
