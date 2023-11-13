package com.wyd.reactor_web.mvc.mhandler;

/**
 * @program: reactor_web
 * @description: BaseMyMethodHandlerFactory关于Spring的实现，实现一些spring接口，并注入spring容器，在项目启动时将所有 MyMethodHandler 准备好
 * @author: Stone
 * @create: 2023-11-13 13:50
 **/
public class SpringMyMethodHandlerFactory extends BaseMyMethodHandlerFactory{

    /**
    * 该类需要注意的事项：
    * */
    // 1. 校验 url 有没有重复的，有的话需要报错
    // 2. 从 Spring 容器中获取 bean，根据 bean 实例和 bean 的类型，获取 MyMethodHandler 加入到集合中
    // 3. 获取 bean 的时候，需要注意时机
    @Override
    protected void initMyMethodHandlers() {

    }
}
