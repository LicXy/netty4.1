package com.lic.demo.rpc.v1.server;

import com.lic.demo.rpc.v1.common.CalculatorService;

public class RpcDemoServer {

	public static void main(String[] args) {

		RpcBuilder builder = new RpcBuilder();
		/**
		 * 需要先注册一个服务类的实例, 后面利用反射执行时需要传入实例对象
		 */
		builder.publishService(CalculatorService.class, new CalculatorServiceImpl());
		
		NettyServer server = new NettyServer(8080);
		try {
			server.start(builder);
		} catch (Exception e) {
 			e.printStackTrace();
		}
		
		 
		
	}

}
