package com.wjg.rpc.config;

import com.wjg.rpc.annotation.RpcServiceAnnotation;
import com.wjg.rpc.register.RpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackages ="com.wjg.rpc")
@ComponentScans(value =
        {@ComponentScan(value = "com.wjg.rpc.service"),
                @ComponentScan(value = "com.wjg.rpc.config")})
public class SpringConfig {

      @Bean(name="rpcService")
      public RpcService getRpcService(){
                  return new RpcService(8080);
      }
}
