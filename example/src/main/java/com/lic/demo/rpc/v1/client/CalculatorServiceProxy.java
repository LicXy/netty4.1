package com.lic.demo.rpc.v1.client;

import com.lic.demo.rpc.v1.common.RpcRequest;
import com.lic.demo.rpc.v1.common.RpcResponse;
import com.lic.demo.rpc.v1.common.SerializerFactory;
import com.lic.demo.rpc.v1.common.CalculatorService;

import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

/**
 * 版本一: 手动生成代理服务方法的代理对象(静态代理)
 */
public class CalculatorServiceProxy implements CalculatorService {

	private NettyClient client;

	public CalculatorServiceProxy(String host, int port) {
		client = new NettyClient();
		try {
			/**
			 * 初始化客户端连接, 当调用对应连接时, 通过客户端发送请求, 来执行服务方法
			 */
			client.init(host, port, SerializerFactory.getSerializer());
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}

	@Override
	public double add(double op1, double op2) {
		String id = UUID.randomUUID().toString();
		/**
		 * 包装Request请求对象:
		 * ServiceName: 指定服务类路径名称
		 * MethodName: 指定目标方法名称
		 * Param1: 指定方法参数一
		 * Param2: 指定方法参数二
		 */
		RpcRequest request = new RpcRequest();
		request.setId(id);
		request.setServiceName(CalculatorService.class.getName());
		request.setMethodName("add");
		request.setParam1(op1);
		request.setParam2(op2);
		/**
		 * 发送自定义RPC请求, 服务端收到请求后, 根据参数执行对应的方法, 返回结果
		 */
		RpcResponse response = this.sendRPCRequest(request);

		return response.getResult();
	}

	@Override
	public double substract(double op1, double op2) {
		String id = UUID.randomUUID().toString();

		RpcRequest request = new RpcRequest();
		request.setServiceName(CalculatorService.class.getName());

		request.setId(id);
		request.setMethodName("sub");
		request.setParam1(op1);
		request.setParam2(op2);

		RpcResponse response = this.sendRPCRequest(request);

		return response.getResult();
	}

	@Override
	public double multiply(double op1, double op2) {
		String id = UUID.randomUUID().toString();

		RpcRequest request = new RpcRequest();
		request.setServiceName(CalculatorService.class.getName());

		request.setId(id);
		request.setMethodName("mul");
		request.setParam1(op1);
		request.setParam2(op2);

		RpcResponse response = this.sendRPCRequest(request);

		return response.getResult();
	}

	private RpcResponse sendRPCRequest(RpcRequest request) {
		SynchronousQueue<RpcResponse> queue = new SynchronousQueue();
		NettyClient.putSunchronousQuee(request.getId(), queue);
		RpcResponse response = null;
		try {
			client.sendRpcRequest(request);
			response = queue.take();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}
	
	public void stop() {
		client.close();
	}

}
