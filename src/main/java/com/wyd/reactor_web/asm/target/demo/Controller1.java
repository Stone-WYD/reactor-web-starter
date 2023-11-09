package com.wyd.reactor_web.asm.target.demo;


import com.wyd.reactor_web.common.AjaxResult;
import com.wyd.reactor_web.common.AjaxResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/asmTest")
public class Controller1 {

    private static final Logger log = LoggerFactory.getLogger(Controller1.class);

    @RequestMapping("/myOwnTest")
    public AjaxResult<Object> test5(@RequestBody User user ){
        System.out.println("调用/asmTest/myOwnTest：test5 啦，传参为：" + user);
        return AjaxResultUtil.getTrueAjaxResult(new AjaxResult<>());
    }

    @PostMapping("/test2")
    public ModelAndView test2(@RequestParam("name") String name) {
        log.debug("test2({})", name);
        return null;
    }

}
