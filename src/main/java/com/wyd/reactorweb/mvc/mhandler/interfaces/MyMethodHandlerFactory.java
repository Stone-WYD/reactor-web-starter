package com.wyd.reactorweb.mvc.mhandler.interfaces;

import com.wyd.reactorweb.mvc.mhandler.entity.MyMethodHandler;

import java.util.List;

public interface MyMethodHandlerFactory {

    List<MyMethodHandler> getMyMethodHandlers();

}
