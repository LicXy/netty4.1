package com.lic.demo.rpc.v1.common;

import java.util.List;

import com.lic.demo.rpc.v1.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RpcDecode extends ByteToMessageDecoder {

	private Class<?> genericClass;
	private Serializer serializer;

	public RpcDecode(Class<?> genericClass, Serializer serializer) {
		this.genericClass = genericClass;
		this.serializer = serializer;
	}

	@Override
	public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (dataLength < 0) {
			ctx.close();
		}
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);

		Object obj = serializer.deserialize(data, genericClass);
		out.add(obj);
	}
}