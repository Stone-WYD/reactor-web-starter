import com.wyd.reactor_web.MySpringApplication;
import com.wyd.reactor_web.mvc.mhandler.SpringMyMethodHandlerFactory;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodHandler;
import com.wyd.reactor_web.mvc.mhandler.SpringMyMethodParameterFactory;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * {@code @program:} reactor_web
 * {@code @description:} spring 测试类
 * {@code @author:} Stone
 * {@code @create:} 2023-11-14 09:33
 **/
@SpringBootTest(classes = MySpringApplication.class)
public class DemoApplicationTest {

    @Resource
    private SpringMyMethodHandlerFactory factory;

    @Resource
    private SpringMyMethodParameterFactory parameterHandlerFactory;

    @Test
    public void testGetMyMethodHandlers1() {
        List<MyMethodHandler> myMethodHandlers = factory.getMyMethodHandlers();
        MyMethodHandler myMethodHandler = myMethodHandlers.get(0);
        myMethodHandler.invoke("/asmTest/test1", new Object[]{});
    }

    @Test
    public void testGetMyMethodHandlers2() {
        List<MyMethodHandler> myMethodHandlers = factory.getMyMethodHandlers();
        MyMethodHandler myMethodHandler = myMethodHandlers.get(0);
        myMethodHandler.invoke("/asmTest/test2", new Object[]{"wyd", "yxy", 1314, "~"});
    }

    @Test
    public void testGetParameterMap() {
        Map<String, MyMethodParameter[]> map = parameterHandlerFactory.getParameterMap();
        for (Map.Entry<String, MyMethodParameter[]> entry : map.entrySet()) {
            String key = entry.getKey();
            MyMethodParameter[] parameters = entry.getValue();
            System.out.println(key + ":");
            for (MyMethodParameter parameter : parameters) {
                System.out.println(parameter);
            }
            System.out.println("==============================");
        }
    }
}
