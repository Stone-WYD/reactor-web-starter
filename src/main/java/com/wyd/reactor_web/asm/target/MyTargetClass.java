package com.wyd.reactor_web.asm.target;

import com.wyd.reactor_web.asm.target.demo.Controller1;
import com.wyd.reactor_web.asm.target.demo.MyInvoke;
import com.wyd.reactor_web.asm.target.demo.User;

import java.lang.reflect.Method;

/**
 * @program: reactor_web
 * @description: 我的目标类
 * @author: Stone
 * @create: 2023-11-09 10:18
 **/
public class MyTargetClass implements MyInvoke {

    // 根据方法编号, 正常调用目标对象方法
    public Object invoke(String urlPath, Object target, Object[] args) {
        if (urlPath.equals("/asmTest/test2")) {
            return ((Controller1) target).test2((String) args[0]);
        } else if (urlPath.equals("/asmTest/myOwnTest")) {
            return ((Controller1) target).test5((User) args[0]);
        }
        else if (urlPath.equals("/asmTest/myOwnTest")) {
            return ((Controller1) target).test5((User) args[0]);
        }
        else if (urlPath.equals("/asmTest/myOwnTest")) {
            return ((Controller1) target).test5((User) args[0]);
        }
        else {
            throw new RuntimeException("无此方法");
        }
    }
}
