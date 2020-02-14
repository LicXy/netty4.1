package com.lic.demo.rpc.v1.common;

/**
 * ====== 版本一 ======
 * 客户端:
 *   1. 客户端调用目标方法
 *   2. 客户端封装Request请求对象(服务类名, 方法名称, 方法参数值1, 方法参数值2)
 *   3. 向服务端发送请求, 等待服务端执行完毕返回结果
 *   4. 获取结果, 输出
 *
 * 服务端:
 *   1. 服务端启动时注册目标服务的实例对象
 *   2. 接收到客户端的请求信息后, 利用反射执行目标方法
 *   3. 目标方法执行完成, 封装Response对象, 返回
 *
 * 缺陷:
 *   1. 静态代理
 *   2. Request实例中封装的方法参数固定, 不可扩展
 *
 */
public interface CalculatorService {
	
	double add(double op1, double op2);
	
	double substract(double op1, double op2);
	
	double multiply(double op1, double op2);
}
