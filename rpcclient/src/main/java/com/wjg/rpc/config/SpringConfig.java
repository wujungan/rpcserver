package com.wjg.rpc.config;

import com.wjg.rpc.proxy.RpcProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackages ="com.wjg.rpc")
@ComponentScans(value =
        {@ComponentScan(value = "com.wjg.rpc.proxy"),
                @ComponentScan(value = "com.wjg.rpc.config")})
public class SpringConfig {

      @Bean(name="rpcService")
      public RpcProxy getRpcService(){
                  return new RpcProxy();
      }
}
