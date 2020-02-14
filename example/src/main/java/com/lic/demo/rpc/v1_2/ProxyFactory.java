package com.lic.demo.rpc.v1_2;

import com.lic.demo.rpc.v1_1.client.NettyClient;

import java.lang.reflect.InvocationHandler;


public final class ProxyFactory {
    /**
     * 使用JDK动态代理创建代理对象
     */
    public static <T> T getProxy(Class<T> interfaceClass, NettyClient proxyInvoker) {
        /**
         * 1. 创建Handler, 增强逻辑
         */
        InvocationHandler handler = new JDKInvocationHandler(interfaceClass, proxyInvoker);
        /**
         * 2. 获取类加载器
         */
        ClassLoader classLoader = ProxyFactory.class.getClassLoader();
        /**
         * 3. 获取代理对象
         */
        T proxy = (T) java.lang.reflect.Proxy.newProxyInstance(classLoader,new Class[] { interfaceClass }, handler);
        return proxy;
    }
    
}