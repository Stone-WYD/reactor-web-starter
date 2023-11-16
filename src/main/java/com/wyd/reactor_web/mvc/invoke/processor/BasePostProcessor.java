package com.wyd.reactor_web.mvc.invoke.processor;

public interface BasePostProcessor <T>{

    default boolean support(PostContext<T> postContext){return true;}

    default  boolean handleBefore(PostContext<T> postContext){
        return true;
    }

    default void handleAfter(PostContext<T> postContext){}

    default int getPriprity(){
        return 0;
    }
}
