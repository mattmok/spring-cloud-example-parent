package com.tiamaes.cloud.netty;

import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.tiamaes.cloud.netty.protocol.message.Header;
import com.tiamaes.cloud.netty.protocol.message.Received;
import com.tiamaes.cloud.netty.protocol.utils.Xor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class CustomizeProtocolDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception{
		byte[] bytes = new byte[in.readableBytes()];
		in.getBytes(0, bytes);
		
		if (Xor.check(bytes)) {
			Header header = new Header();
			// 1消息ID
			header.setId(in.readUnsignedShort());
			int bodyProperties = in.readUnsignedShort();
			header.setChildPackage((bodyProperties >> 13 & 1) == 1);
			header.setEnableRSA((bodyProperties >> 14 & 1) == 1);
			header.setLength(bodyProperties & 0x03FF);
			
			byte[] bytes1 = new byte[6];
			in.readBytes(bytes1);
			String terminalId = Hex.encodeHexString(bytes1);
			header.setTerminalId(terminalId);
			
			byte[] bytes2 = new byte[6];
			in.readBytes(bytes2);
			String terminalNo = Hex.encodeHexString(bytes2);
			header.setTerminalNo(terminalNo);
			header.setTerminalType(Header.Type.values()[in.readUnsignedByte()]);
			header.setSerialNo(in.readUnsignedShort());
			if (header.isChildPackage()) {
				header.setPacketTotal(in.readUnsignedShort());
				header.setPacketNo(in.readUnsignedShort());
			}
			byte[] bytes3 = new byte[in.readableBytes() - 1];
			in.readBytes(bytes3);
			
			Received received = Received.getInstance(header, bytes3);
			if(received != null){
				out.add(received);
			}
		}
		
	}
}