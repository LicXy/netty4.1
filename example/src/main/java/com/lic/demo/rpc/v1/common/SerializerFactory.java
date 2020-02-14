package com.lic.demo.rpc.v1.common;

import com.lic.demo.rpc.v1.serialize.ProtobufSerializer;
import com.lic.demo.rpc.v1.serialize.Serializer;

public class SerializerFactory {
	
	public static Serializer getSerializer() {
		return new ProtobufSerializer();
	}

}
