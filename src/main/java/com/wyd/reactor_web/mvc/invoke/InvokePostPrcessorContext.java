package com.wyd.reactor_web.mvc.invoke;

import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodHandler;
import com.wyd.reactor_web.mvc.mhandler.entity.MyMethodParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: reactor_web
 * @description: 后处理上下文
 * @author: Stone
 * @create: 2023-11-16 13:56
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvokePostPrcessorContext {

    private MyMethodHandler myMethodHandler;

    private MyMethodParameter[] myMethodParameters;

    private Object[] parameters;

    private Object invokeResult;
}
