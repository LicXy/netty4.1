package com.lic.demo.rpc.v1.server;

 
import com.lic.demo.rpc.v1.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
 
public class NettyServer   {
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	private Thread thread;
	
	private final int port;
	
	public NettyServer(int port) {
		this.port = port;
	}

 	public void start(final RpcBuilder rpcBuilder) throws Exception {
		/**
		 * 起一个线程跑服务端
		 */
		thread = new Thread(new Runnable() {
			@Override
			public void run() {

				EventLoopGroup bossGroup = new NioEventLoopGroup();
				EventLoopGroup workerGroup = new NioEventLoopGroup();
				try {
					ServerBootstrap bootstrap = new ServerBootstrap();
					bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
							.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel channel) throws Exception {
									channel.pipeline()
											.addLast(new RpcDecode(RpcRequest.class, SerializerFactory.getSerializer()))
											.addLast(new RpcEncode(RpcResponse.class, SerializerFactory.getSerializer()))
											.addLast(new NettyServerHandler(rpcBuilder));
								}
							})
							.option(ChannelOption.SO_TIMEOUT, 100)
							.option(ChannelOption.SO_BACKLOG, 128)
							.option(ChannelOption.TCP_NODELAY, true)
							.option(ChannelOption.SO_REUSEADDR, true)
							.childOption(ChannelOption.SO_KEEPALIVE, true);
					ChannelFuture future = bootstrap.bind(port).sync();

                    logger.info("接口服务器端启动成功......");
 
					Channel serviceChannel = future.channel().closeFuture().sync().channel();
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				} finally {
					workerGroup.shutdownGracefully();
					bossGroup.shutdownGracefully();
				}
			}
		});
		/**
		 * 启动线程
		 */
		thread.start();

	}
 	
 	public Thread getThread() {
 		return this.thread;
 	}

 	public void stop() throws Exception {
        if (thread!=null && thread.isAlive()) {
            thread.interrupt();
        }

         logger.info("接口服务器端停止......");
	}

}
