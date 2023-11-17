package com.wyd.reactor_web.design.postprocessor;

public interface BasePostProcessor <T>{

    default boolean support(T postContext){return true;}

    default  boolean handleBefore(T postContext){
        return true;
    }

    default void handleAfter(T postContext){}

    default int getPriprity(){
        return 0;
    }
}
