package com.lic.demo.rpc.cust.server;


import com.lic.demo.rpc.v1_1.server.NettyServer;
import com.lic.demo.rpc.v1_1.server.RpcBuilder;
import com.lic.demo.rpc.v1_2.CalculatorService;
import com.lic.demo.rpc.v1_2.CalculatorServiceImpl;

public class RpcDemoServer {

	public static void main(String[] args) {
		/**
		 * 创建RpcBuilder对象
		 * 该对象用于发布服务信息以及通过反射调用服务方法
		 */
		RpcBuilder builder = new RpcBuilder();
		//发布服务
		builder.publishService(CalculatorService.class, new CalculatorServiceImpl());
		//创建服务端, 启动
		NettyServer server = new NettyServer(8080);
		try {
			server.start(builder);
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
}
