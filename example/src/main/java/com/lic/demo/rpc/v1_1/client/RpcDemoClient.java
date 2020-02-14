package com.lic.demo.rpc.v1_1.client;

public class RpcDemoClient {

	public static void main(String[] args) {
		CalculatorServiceProxy proxy = new CalculatorServiceProxy("127.0.0.1", 8080);
		
		System.out.println(proxy.add(10.0, 2.0, 3.0));
		
		proxy.stop();
	}

}
