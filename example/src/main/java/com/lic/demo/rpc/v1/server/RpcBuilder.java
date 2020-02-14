package com.lic.demo.rpc.v1.server;

import com.lic.demo.rpc.v1.common.RpcException;
import com.lic.demo.rpc.v1.common.RpcRequest;
import com.lic.demo.rpc.v1.common.RpcResponse;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

 
public class RpcBuilder {
	Map<String, Object> services = new ConcurrentHashMap();
	Map<String, Class> interfaces = new ConcurrentHashMap();

	public void publishService(Class serviceInterface, Object service) {
		if (services.containsKey(serviceInterface.getName()) || interfaces.containsKey(serviceInterface.getName())) {
			throw new RpcException("serviceInterface has been already registered......");
		}

		if (!(serviceInterface.isInstance(service))) {
			throw new RpcException("service object must implement the service Interface .......");
		}

		services.put(serviceInterface.getName(), service);
		interfaces.put(serviceInterface.getName(), serviceInterface);
	}

	public RpcResponse invokeService(RpcRequest rpcRequest) {
		String serviceName = rpcRequest.getServiceName(); //拿到接口名
		String methodName = rpcRequest.getMethodName(); //拿到方法名，add
		Class serviceInterface = interfaces.get(serviceName); //拿到具体的接口，CalculatorService
		double p1 = rpcRequest.getParam1(); //拿到请求的参数
		double p2 = rpcRequest.getParam2();
		
		Method m;
		Object result = null;
		try {
			m = serviceInterface.getMethod(methodName, new Class[] {double.class, double.class});
			result = m.invoke(services.get(serviceName), new Object[] {p1, p2});

		} catch (Exception e) {
 			e.printStackTrace();
		}
		/**
		 * 封装Response对象,返回
		 */
		RpcResponse response = new RpcResponse();
		response.setId(rpcRequest.getId());
		response.setServiceName(serviceName);
		response.setMethodName(methodName);
		response.setResult((Double) result);
		

 		return response;
	}

}
