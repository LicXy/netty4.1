package com.lic.demo.rpc.v1_1.server;

import com.lic.demo.rpc.v1_1.common.RpcRequest;
import com.lic.demo.rpc.v1_1.common.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

 public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private RpcBuilder rpcBuilder;
    public NettyServerHandler(RpcBuilder rpcBuilder) {
        this.rpcBuilder = rpcBuilder;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        /**
         * 解析Request, 执行目标方法
         */
         RpcResponse rpcResponse = rpcBuilder.invokeService(rpcRequest);
        /**
         * 返回结果
         */
        ctx.writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         ctx.close();
    }
}
