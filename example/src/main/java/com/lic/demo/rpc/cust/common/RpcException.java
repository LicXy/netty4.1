package com.lic.demo.rpc.cust.common;

public class RpcException extends RuntimeException {

	public RpcException(String msg) {
		super(msg);
	}

	public RpcException(String message, Exception e) {
		super(message, e);
 	}

}
