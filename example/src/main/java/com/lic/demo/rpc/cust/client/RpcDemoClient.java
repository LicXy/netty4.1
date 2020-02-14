package com.lic.demo.rpc.cust.client;

import com.lic.demo.rpc.v1.common.SerializerFactory;
import com.lic.demo.rpc.v1_1.client.NettyClient;
import com.lic.demo.rpc.v1_2.CalculatorService;
import com.lic.demo.rpc.v1_2.ProxyFactory;

public class RpcDemoClient {

	public static void main(String[] args) throws Exception {
		// 创建客户端
		NettyClient client = new NettyClient();
		// 初始化客户端
		client.init("127.0.0.1", 8080, SerializerFactory.getSerializer());
		/**
		 * 获取代理类
		 */
		CalculatorService service = ProxyFactory.getProxy(CalculatorService.class, client);
		/**
         * 执行目标方法
		 */
		System.out.println(service.add(1.0, 156.0));
		
		client.close();
		
	}

}
