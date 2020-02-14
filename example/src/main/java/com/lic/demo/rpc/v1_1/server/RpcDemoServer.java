package com.lic.demo.rpc.v1_1.server;


import com.lic.demo.rpc.v1_1.common.CalculatorService;
import com.lic.demo.rpc.v1_1.server.CalculatorServiceImpl;
import com.lic.demo.rpc.v1_1.server.NettyServer;
import com.lic.demo.rpc.v1_1.server.RpcBuilder;

public class RpcDemoServer {

	public static void main(String[] args) {

		RpcBuilder builder = new RpcBuilder();
		/**
		 * 注册服务类实例
		 */
		builder.publishService(CalculatorService.class, new CalculatorServiceImpl());
		/**
		 * 启动服务端线程
		 */
		NettyServer server = new NettyServer(8080);
		try {
			server.start(builder);
		} catch (Exception e) {
 			e.printStackTrace();
		}
		
		 
		
	}

}
