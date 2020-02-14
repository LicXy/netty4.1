package com.lic.demo.rpc.v1_1.client;

import com.lic.demo.rpc.v1_1.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.SynchronousQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("远程连接异常：", cause);
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
		String id = rpcResponse.getId();
		SynchronousQueue<RpcResponse> synchronousQueue = NettyClient.getSynchronousQueue(id);
		synchronousQueue.put(rpcResponse);
		NettyClient.removeById(id);

	}

}
