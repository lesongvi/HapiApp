package com.hapi.hapiservice.helpers.core.common;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface HapiBot {
    String value() default "";

}