import com.wyd.reactor_web.asm.BaseMyMethodHandlerFactory;
import com.wyd.reactor_web.asm.MyMethodHandler;

import java.util.List;

/**
 * @program: reactor_web
 * @description: 测试类
 * @author: Stone
 * @create: 2023-11-10 18:14
 **/
public class MyTest {

    public static void main(String[] args) throws Exception {
        BaseMyMethodHandlerFactory factory = new BaseMyMethodHandlerFactory();
        factory.addMyMethodHandler(new Controller1(), Controller1.class);
        MyMethodHandler myMethodHandler = factory.getMyMethodHandlers().get(0);
        myMethodHandler.invoke("/asmTest/test2", new Object[]{"123"});
    }
}
