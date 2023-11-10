/**
 * @program: reactor_web
 * @description: 测试controller
 * @author: Stone
 * @create: 2023-11-10 18:16
 **/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/asmTest")
public class Controller1 {

    private static final Logger log = LoggerFactory.getLogger(Controller1.class);


    @PostMapping("/test2")
    public ModelAndView test2(@RequestParam("name") String name) {
        log.debug("test2({})", name);
        System.out.println(name);
        return null;
    }

}
