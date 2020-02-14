package com.lic.demo.rpc.cust.client;

import com.lic.demo.rpc.v1_1.client.NettyClient;
import com.lic.demo.rpc.v1_1.common.RpcRequest;
import com.lic.demo.rpc.v1_1.common.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

public class JDKInvocationHandler implements InvocationHandler {
	private NettyClient client;
	private Class<?> proxyClass;

	public JDKInvocationHandler(Class<?> proxyClass, NettyClient client) {
		this.proxyClass = proxyClass;
		this.client = client;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] paramValues) throws Throwable {
		String methodName = method.getName();
		Class<?>[] paramTypes = method.getParameterTypes();
		/**
         * 如果执行的是toString(), hashCode(), equals()方法,则调用原方法
		 */
		if ("toString".equals(methodName) && paramTypes.length == 0) {
			return client.toString();
		} else if ("hashCode".equals(methodName) && paramTypes.length == 0) {
			return client.hashCode();
		} else if ("equals".equals(methodName) && paramTypes.length == 1) {
			Object another = paramValues[0];
			return proxy == another || (proxy.getClass().isInstance(another) && client.equals(parseInvoker(another)));
		}

        /**
         * 根据目标方法信息封装Request请求信息
		 */
		RpcRequest request = buildRequest(proxyClass.getName(), method, paramValues);
		/**
         * 发送RPC请求, 让服务端执行目标方法
		 */
		RpcResponse response = sendRPCRequest(request);

		return response.getResult();
	}


	private RpcResponse sendRPCRequest(RpcRequest request) {
		//创建同步队列, 等待返回数据
		SynchronousQueue<RpcResponse> queue = new SynchronousQueue();
		NettyClient.putSunchronousQuee(request.getId(), queue);
		RpcResponse response = null;
		try {
			/**
             * 客户端发送请求
			 */
			client.sendRpcRequest(request);
			/**
			 * 等待服务器端返回数据, 然后客户端处理器会将返回的response对象放入同步对列中
			 */
			response = queue.take();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}
	
    public static NettyClient parseInvoker(Object proxyObject) {
        InvocationHandler handler = java.lang.reflect.Proxy.getInvocationHandler(proxyObject);
        if (handler instanceof JDKInvocationHandler) {
            return ((JDKInvocationHandler) handler).getProxyInvoker();
        }
        return null;
    }
    
	public Class<?> getProxyClass() {
		return proxyClass;
	}

	public NettyClient getProxyInvoker() {
        return client;
    }

	private RpcRequest buildRequest(String serviceName, Method method, Object[] args) {
		/**
		 * 包装Request
		 */
		String id = UUID.randomUUID().toString();

		RpcRequest request = new RpcRequest();
		request.setServiceName(serviceName);
		request.setId(id);
		request.setMethodName(method.getName());
		request.setArgs(args); //设置参数值
        //遍历所有参数, 获取参数类型集合
		List<String> parameterTypes = new ArrayList<String>();
		for (Class<?> parameterType : method.getParameterTypes()) {
			parameterTypes.add(parameterType.getName());
		}
        //参数类型信息
		request.setParameterTypeNames(parameterTypes.toArray(new String[0]));
		return request;
	}
}
