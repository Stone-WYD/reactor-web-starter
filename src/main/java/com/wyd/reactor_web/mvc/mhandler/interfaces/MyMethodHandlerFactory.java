package com.wyd.reactor_web.mvc.mhandler.interfaces;

import com.wyd.reactor_web.mvc.mhandler.MyMethodHandler;

import java.util.List;

public interface MyMethodHandlerFactory {

    List<MyMethodHandler> getMyMethodHandlers();

}
