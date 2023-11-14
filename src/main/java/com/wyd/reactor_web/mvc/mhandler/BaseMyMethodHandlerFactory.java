package com.wyd.reactor_web.mvc.mhandler;

import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodHandler;
import com.wyd.reactor_web.mvc.mhandler.interfaces.MyMethodHandlerFactory;
import com.wyd.reactor_web.mvc.mhandler.assist.GenerateClassUtil;
import org.springframework.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wyd.reactor_web.mvc.util.MapperUtil.getPathSet;

/**
 * @program: reactor_web
 * @description: 获取方法调用类，定义为抽象类，避免直接创建该类
 * @author: Stone
 * @create: 2023-11-10 16:26
 **/
public abstract class BaseMyMethodHandlerFactory extends ClassLoader  implements MyMethodHandlerFactory, Opcodes {

    private final List<MyMethodHandler> myMethodHandlers = new ArrayList<>();

    private final AtomicInteger increNameNum = new AtomicInteger(0);

    abstract void init();

    @Override
    public List<MyMethodHandler> getMyMethodHandlers() {
        return myMethodHandlers;
    }

    protected void addMyMethodHandler(Object target, Class<?> invokeClass) throws Exception {
        // 获取需要动态生成类的类名
        String generateClassName = "MyInvoke" + increNameNum.addAndGet(1);
        // 获取动态类字节码
        byte[] code = GenerateClassUtil.generate(generateClassName, invokeClass);
        // 生成动态类的实例对象
        if (code == null) {
            throw new RuntimeException("GenerateClassUtil.generate: " + generateClassName + "生成动态类失败！");
        }
        Class<?> aClass = defineClass(generateClassName, code, 0, code.length);
        MyMethodHandler.MyInvoke myInvoke = (MyMethodHandler.MyInvoke) aClass.getConstructor().newInstance();
        // 根据实例对象生成 MyMethodHandler
        MyMethodHandler myMethodHandler = new MyMethodHandler(myInvoke, getPathSet(invokeClass), target);
        // MyMethodHandler 加入 list
        myMethodHandlers.add(myMethodHandler);
    }

}
