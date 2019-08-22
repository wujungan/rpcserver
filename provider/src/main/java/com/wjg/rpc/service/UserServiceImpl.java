package com.wjg.rpc.service;

import com.wjg.rpc.annotation.RpcServiceAnnotation;
import com.wjg.rpc.domain.User;

import java.util.UUID;

@RpcServiceAnnotation(Userservice.class)
public class UserServiceImpl implements  Userservice {
    public User addUser(User user) {
        user=new User("wujungan", UUID.randomUUID().toString(),20);
        System.out.println(user);
        return user;
    }

    public User getUser(String id) {
      User  user=new User("wujungan"+id, id,20);
        return user;
    }
}
