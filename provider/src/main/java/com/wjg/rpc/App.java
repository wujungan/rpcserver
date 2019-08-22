package com.wjg.rpc;

import com.wjg.rpc.config.SpringConfig;
import com.wjg.rpc.register.RpcService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        context.start();
    }
}
