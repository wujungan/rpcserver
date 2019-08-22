package com.wjg.rpc.register;

import com.wjg.rpc.transport.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToByteEncoder;

import java.lang.reflect.Method;
import java.util.Map;

public class RegistryHandler extends ChannelInboundHandlerAdapter  {
    Map<String,Object> handlerMap;
    public RegistryHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String className="";
        String methodNme="";
        Object service=null;
        Object[] params=null;
        Class<?>[] paramtypes=null;
        //获取merhod
        Method method=null;

        if(msg instanceof InvokerProtocol){
            InvokerProtocol invokerProtocol = (InvokerProtocol) msg;
            className = invokerProtocol.getClassName();
            service = handlerMap.get(className);
            methodNme = invokerProtocol.getMethodNme();
            params = invokerProtocol.getParams();
            paramtypes = invokerProtocol.getParamtypes();
        }


        Class clazz=Class.forName(className); //跟去请求的类进行加载
        method = clazz.getMethod(methodNme, paramtypes);

        Object result = method.invoke(service, params);

        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
         ctx.close();
    }
}
