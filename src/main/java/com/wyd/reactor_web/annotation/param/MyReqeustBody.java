package com.wyd.reactor_web.annotation.param;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyReqeustBody {
}
