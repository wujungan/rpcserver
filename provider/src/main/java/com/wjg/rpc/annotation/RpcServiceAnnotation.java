package com.wjg.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)//类/接口
@Retention(RetentionPolicy.RUNTIME)
@Component//spring 扫描
public @interface RpcServiceAnnotation {
      Class<?> value();
     String version() default "";

}
