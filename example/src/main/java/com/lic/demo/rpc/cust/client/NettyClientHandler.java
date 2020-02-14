package com.lic.demo.rpc.cust.client;

import com.lic.demo.rpc.v1_1.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;

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
		/**
		 * 将从接收到服务器返回的结果放入同步对列中
		 */
		SynchronousQueue<RpcResponse> synchronousQueue = NettyClient.getSynchronousQueue(id);
		synchronousQueue.put(rpcResponse);

		NettyClient.removeById(id);
	}

}
