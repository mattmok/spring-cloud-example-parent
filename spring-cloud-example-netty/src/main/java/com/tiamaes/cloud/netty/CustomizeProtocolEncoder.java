package com.tiamaes.cloud.netty;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tiamaes.cloud.netty.protocol.message.Body;
import com.tiamaes.cloud.netty.protocol.message.Header;
import com.tiamaes.cloud.netty.protocol.message.Message;
import com.tiamaes.cloud.netty.protocol.utils.ToASCII;
import com.tiamaes.cloud.netty.protocol.utils.Xor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Component
@ChannelHandler.Sharable
public class CustomizeProtocolEncoder extends MessageToByteEncoder<Message> {
	private static final Logger logger = LogManager.getLogger(CustomizeProtocolEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
		Header header = message.getHeader();
		Body body = message.getBody();
		if(header.getSerialNo() == 0){
			//TODO 生成消息序列号
			header.setSerialNo(0);
		}

		byte[] array1 = header.toBytes();
		byte[] array2 = body.toBytes();

		byte[] array3 = ArrayUtils.addAll(array1, array2);

		byte checksum = Xor.xorValue(array3);
		array3 = ArrayUtils.add(array3, checksum);
		// 这里需要转义
		// 0x7e <————> 0x7d 后紧跟一个 0x02；
		// 0x7d <————> 0x7d 后紧跟一个 0x01。
		CompositeByteBuf buffer = Unpooled.compositeBuffer();	
		for (byte arr : array3) {
			if (arr == 0x7e) {
				buffer.writeByte(0x7d);
				buffer.writeByte(0x02);
			} else if (arr == 0x7d) {
				buffer.writeByte(0x7d);
				buffer.writeByte(0x01);
			} else {
				buffer.writeByte(arr);
			}
		}
		array3 = new byte[buffer.readableBytes()];
		buffer.readBytes(array3);
		if (header.getTerminalType().equals(Header.Type.STATION)) {
			array3 = ArrayUtils.add(array3, 0, (byte) 0x7e);
			array3 = ArrayUtils.add(array3, (byte) 0x7e);
		} else if(header.getTerminalType().equals(Header.Type.LOCK)){
			String param = Hex.encodeHexString(array3);
			String str = ToASCII.StringToASCII1(param);
			array3 = Hex.decodeHex(str.toCharArray());
			array3 = ArrayUtils.add(array3, 0, (byte) 0x45);
			array3 = ArrayUtils.add(array3, 0, (byte) 0x37);
			array3 = ArrayUtils.add(array3, (byte) 0x37);
			array3 = ArrayUtils.add(array3, (byte) 0x45);
		}

		
		buffer.release();
		if (logger.isTraceEnabled()) {
			logger.trace(Hex.encodeHexString(array3));
		}

		out.writeBytes(array3);
	}

}