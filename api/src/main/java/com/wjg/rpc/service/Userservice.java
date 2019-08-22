package com.wjg.rpc.service;

import com.wjg.rpc.domain.User;

public interface Userservice {

     //
     public User addUser(User user);

     public User getUser(String id);
}
