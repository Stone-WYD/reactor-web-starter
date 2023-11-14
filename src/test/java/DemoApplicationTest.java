import com.wyd.reactor_web.MySpringApplication;
import com.wyd.reactor_web.mvc.mhandler.SpringMyMethodHandlerFactory;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: reactor_web
 * @description: spring 测试类
 * @author: Stone
 * @create: 2023-11-14 09:33
 **/
@SpringBootTest(classes = MySpringApplication.class)
public class DemoApplicationTest {

    @Resource
    private SpringMyMethodHandlerFactory factory;

    @Test
    public void test1() {
        List<MyMethodHandler> myMethodHandlers = factory.getMyMethodHandlers();
        MyMethodHandler myMethodHandler = myMethodHandlers.get(0);
        myMethodHandler.invoke("/asmTest/test1", new Object[]{});
    }

    @Test
    public void test2() {
        List<MyMethodHandler> myMethodHandlers = factory.getMyMethodHandlers();
        MyMethodHandler myMethodHandler = myMethodHandlers.get(0);
        myMethodHandler.invoke("/asmTest/test2", new Object[]{"wyd", "yxy", 1314, "~"});
    }
}
