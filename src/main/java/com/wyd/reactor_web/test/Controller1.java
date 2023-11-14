package com.wyd.reactor_web.test;

import com.wyd.reactor_web.annotation.MyRequestMapping;
import com.wyd.reactor_web.annotation.MyRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;


/**
 * @program: reactor_web
 * @description: 测试controller
 * @author: Stone
 * @create: 2023-11-10 18:16
 **/
@Slf4j
@Component
@MyRequestMapping(value = "/asmTest")
public class Controller1 {

    @MyRequestMapping(value = "/test1")
    public ModelAndView test1() {
        System.out.println("没有传参的测试~");
        return null;
    }

    @MyRequestMapping(value = "/test2")
    public ModelAndView test2(@MyRequestParam("name") String name, String name2, Integer name3, String name4) {
        String content = name + name2 + name3 + name4;
        log.debug("test2({})", content);
        System.out.println(content);
        return null;
    }



}
