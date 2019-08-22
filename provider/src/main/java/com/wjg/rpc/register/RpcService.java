package com.wjg.rpc.register;

import com.wjg.rpc.annotation.RpcServiceAnnotation;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RpcService implements ApplicationContextAware, InitializingBean {

    ExecutorService executorService= Executors.newCachedThreadPool();

    private Map<String,Object> handlerMap=new HashMap();

    private int port;

    public RpcService(int port) {
        this.port = port;
    }

    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    //对象参数类型编码器
                    pipeline.addLast("encoder",new ObjectEncoder());
                    //对象参数类型解码器
                    pipeline.addLast("decoder",new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                    pipeline.addLast(new RegistryHandler(handlerMap));
                }
            });
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(port).sync();
            System.out.println("GP RPC Registry start listen at " + port );
            System.out.println(handlerMap.toString() );

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RpcServiceAnnotation.class);
          if(!beanMap.isEmpty()){
              Collection<Object> values = beanMap.values();
              for(Object beanService:values){
                  RpcServiceAnnotation annotation = beanService.getClass().getAnnotation(RpcServiceAnnotation.class);

                  //拿到接口类定义
                  String serviceName = annotation.value().getName();
                  handlerMap.put(serviceName,beanService);
              }
          }
    }
}
