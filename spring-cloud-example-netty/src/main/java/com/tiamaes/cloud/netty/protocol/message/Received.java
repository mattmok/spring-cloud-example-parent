package com.tiamaes.cloud.netty.protocol.message;

import java.lang.reflect.Constructor;

import org.apache.commons.codec.binary.Hex;

/**
 * @author Chen
 *
 */
public class Received {

	private Header header;

	private byte[] bytes;
	
	public Received(byte[] bytes){
		this.bytes = bytes;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(header.toString());
		if(this.bytes != null){
			builder.append(Hex.encodeHexString(this.bytes));
		}
		return builder.toString();
	}

	public static Received getInstance(Header header, byte[] bytes) {
		try {
			@SuppressWarnings("unchecked")
			Constructor<Received> clazz = (Constructor<Received>) Class.forName(Received.class.getName() + String.format("%04X", header.getId())).getConstructor(byte[].class);
			Received received = clazz.newInstance(bytes);
			received.setHeader(header);
			return received;
		} catch (Exception e) {
			return null;
		}
	}
}
