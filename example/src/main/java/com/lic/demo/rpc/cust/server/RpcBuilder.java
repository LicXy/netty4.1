package com.lic.demo.rpc.cust.server;

import com.lic.demo.rpc.cust.common.ClassUtils;
import com.lic.demo.rpc.cust.common.RpcException;
import com.lic.demo.rpc.cust.common.RpcRequest;
import com.lic.demo.rpc.cust.common.RpcResponse;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcBuilder {
	Map<String, Object> services = new ConcurrentHashMap();
	Map<String, Class<?>> interfaces = new ConcurrentHashMap();
	/**
	 * 发布服务
	 * @param serviceInterface 服务接口
	 * @param service  服务实例
	 */
	public void publishService(Class<?> serviceInterface, Object service) {
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
		String serviceName = rpcRequest.getServiceName(); //获取服务类名
		String methodName = rpcRequest.getMethodName();  //获取方法名称
		Class<?> serviceInterface = interfaces.get(serviceName); //根据服务名称获取目标接口
		String[] parameterTypeNames = rpcRequest.getParameterTypeNames(); //获取参数类型名称

		Method m;
		Object result = null;
        /**
         * 根据参数类型名称生成参数类型数组, 后面根据此数组可以选择指定的方法
		 */
		Class<?>[] parameterClasses = new Class<?>[parameterTypeNames.length];
		try {
			for (int i = 0; i < parameterClasses.length; i++) {
				parameterClasses[i] = ClassUtils.getClass(parameterTypeNames[i]);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			/**
			 * 根据方法名称以及参数类型数组选择目标方法
			 */
			m = serviceInterface.getMethod(methodName, parameterClasses);
			/**
			 * 执行目标方法
			 */
			result = m.invoke(services.get(serviceName), rpcRequest.getArgs());

		} catch (Exception e) {
			e.printStackTrace();
		}
        /**
         * 封装response对象
		 */
		RpcResponse response = new RpcResponse();
		response.setId(rpcRequest.getId());
		response.setMethodName(methodName);
		response.setResult((Double) result);

		return response;
	}

}
