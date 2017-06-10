package com.tiamaes.cloud.netty;

import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class CustomizeBaseFrameDecoder extends ByteToMessageDecoder {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomizeBaseFrameDecoder.class);
	
	private byte delimiter;
	private byte[] ascii_delimiters;
	
	
	public CustomizeBaseFrameDecoder(byte delimiter){
		this(delimiter, true);
	}
	
	public CustomizeBaseFrameDecoder(byte delimiter, boolean supportAscii){
		this.delimiter = delimiter;
		if(supportAscii == true){
			this.ascii_delimiters = delimiterAsciiCode(this.delimiter);
		}
	}
	
	protected byte[] delimiterAsciiCode(byte delimiter) {
		char[] chars = Hex.encodeHex(new byte[]{delimiter});
		byte[] delimiters = new byte[2];
		for(int i = 0; i < chars.length ; i++){
			delimiters[i] = (byte) Character.toUpperCase(chars[i]);
		}
		return delimiters;
	}
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		if(in instanceof EmptyByteBuf) {
			return;
		}
		int i = 0;
		ByteBuf buffer = null;
		int header = in.readUnsignedByte();
		if (header == delimiter) {
			while (in.readableBytes() > i) {
				i++;
				if(in.getByte(i) == delimiter){
					if(i > 2){
						buffer = in.readSlice(i - 1);
						in.skipBytes(1);
						in.discardReadBytes();
					}
					break;
				}
			}
			in.resetReaderIndex();
		}else if(ascii_delimiters != null && ascii_delimiters.length == 2 && header == ascii_delimiters[0]){
			if(in.readableBytes() > 1){
				int header2 = in.readUnsignedByte();
				if(header2 == ascii_delimiters[1]){
					while (in.readableBytes() > i) {
						i = i + 2;
						if(in.getShort(i) == 0x3745){
							String hex = new String(in.readBytes(i - 2).array());
							try {
								byte[] bytes = Hex.decodeHex(hex.toCharArray());
								buffer = Unpooled.copiedBuffer(bytes);
							} catch (DecoderException e) {
								logger.error(e.getMessage(), e);
							}finally{
								in.skipBytes(2);
								in.discardReadBytes();
							}
							break;
						}
					}
				}
			}
			in.resetReaderIndex();
		}else{
			in.discardReadBytes();
		}
		
		if(buffer != null){
			buffer.retain();
			out.add(buffer);
			if(logger.isTraceEnabled()){
				logger.trace("RCVD: 7E{}7E", ByteBufUtil.hexDump(buffer));
			}
		}
	}
}