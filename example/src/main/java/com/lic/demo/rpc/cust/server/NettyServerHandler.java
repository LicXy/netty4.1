package com.lic.demo.rpc.cust.server;

import com.lic.demo.rpc.cust.common.RpcRequest;
import com.lic.demo.rpc.cust.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

   private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);


   private RpcBuilder rpcBuilder;
   public NettyServerHandler(RpcBuilder rpcBuilder) {
       this.rpcBuilder = rpcBuilder;
   }


   @Override
   public void channelRead0(final ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
       /**
        * 解析Request请求信息, 执行目标方法
        */
       RpcResponse rpcResponse = rpcBuilder.invokeService(rpcRequest);
       //返回执行结果
       ctx.writeAndFlush(rpcResponse);
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
   }
}
