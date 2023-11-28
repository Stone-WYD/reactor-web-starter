package com.wyd.reactorweb.annotation;

import com.wyd.reactorweb.design.reactor.pipeline.Handler;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChannelInfo {

    // 类型不传认为没有 prepare 和 render
    Class<? extends Handler> renderType() default Handler.class;

    Class<? extends Handler> prepareType() default Handler.class;
}
