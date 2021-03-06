package com.lic.demo.rpc.cust.serialize;

public interface Serializer {

	public <T> Object deserialize(byte[] bytes, Class<T> clazz) ;

	public <T> byte[] serialize(T obj);
}
