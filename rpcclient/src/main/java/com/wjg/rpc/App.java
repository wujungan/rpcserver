package com.wjg.rpc;

import com.wjg.rpc.config.SpringConfig;
import com.wjg.rpc.domain.User;
import com.wjg.rpc.proxy.RpcProxy;
import com.wjg.rpc.service.Userservice;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        RpcProxy proxy = context.getBean(RpcProxy.class);
        Userservice userservice = proxy.create(Userservice.class,"127.0.0.1",8080);
        User user = userservice.addUser(new User());
        System.out.println(user);

        User user1 = userservice.getUser("123");
        System.out.println(user1);

    }
}
