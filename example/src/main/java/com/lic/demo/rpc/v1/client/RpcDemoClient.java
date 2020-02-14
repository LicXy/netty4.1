package com.lic.demo.rpc.v1.client;

import com.lic.demo.rpc.v1.client.CalculatorServiceProxy;

public class RpcDemoClient {

	public static void main(String[] args) {
		CalculatorServiceProxy proxy = new CalculatorServiceProxy("127.0.0.1", 8080);
		
		System.out.println(proxy.add(1.0, 2.0));
		
		proxy.stop();
	}

}
