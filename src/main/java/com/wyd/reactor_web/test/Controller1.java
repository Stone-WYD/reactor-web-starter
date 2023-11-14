package com.wyd.reactor_web.test; /**
 * @program: reactor_web
 * @description: 测试controller
 * @author: Stone
 * @create: 2023-11-10 18:16
 **/

import com.wyd.reactor_web.annotation.MyRequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@MyRequestMapping(value = "/asmTest")
public class Controller1 {


    @MyRequestMapping(value = "/test2")
    public ModelAndView test2(@RequestParam("name") String name) {
        log.debug("test2({})", name);
        System.out.println(name);
        return null;
    }

}
