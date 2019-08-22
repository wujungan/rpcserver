package com.wjg.rpc.proxy;

import com.wjg.rpc.transport.InvokerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {



    public  <T>T create(Class<T> clazz, String host, int port){
        Class[] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
       return  (T)Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,new MethodProxy(clazz,host,port));
    }

    public  class MethodProxy implements InvocationHandler{

        private Class<?> clazz;

        String host;
        int port;

        public MethodProxy(Class<?> clazz, String host, int port) {
            this.clazz = clazz;
            this.host = host;
            this.port = port;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //如果传进来是一个已实现的具体类（本次演示略过此逻辑)
            if (Object.class.equals(method.getDeclaringClass())) {
                try {
                    return method.invoke(this, args);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                //如果传进来的是一个接口（核心)
            } else {
                return rpcInvoke(proxy,method, args);
            }
            return null;
        }

        private Object rpcInvoke(Object proxy, Method method, Object[] args) {
          InvokerProtocol invokerProtocol = new InvokerProtocol();
            invokerProtocol.className(this.clazz.getName())
                    .methodNme(method.getName())
                    .paramtypes(method.getParameterTypes())
                    .params(args);

            final RpcProxyHandler consumerHandler = new RpcProxyHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                //对象参数类型编码器
                                pipeline.addLast("encoder", new ObjectEncoder());
                                //对象参数类型解码器
                                pipeline.addLast("decoder", new ObjectDecoder( ClassResolvers.cacheDisabled(null)));
                                pipeline.addLast("handler",consumerHandler);
                            }
                        });

                ChannelFuture future = b.connect("localhost", 8080).sync();
                future.channel().writeAndFlush(invokerProtocol).sync();
                future.channel().closeFuture().sync();
            } catch(Exception e){
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }

          return consumerHandler.getResponse();
        }
    }
}
