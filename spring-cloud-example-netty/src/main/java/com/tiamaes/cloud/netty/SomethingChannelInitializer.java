package com.tiamaes.cloud.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

@Component
@Qualifier("somethingChannelInitializer")
public class SomethingChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	@Autowired
	@Qualifier("somethingServerHandler")
	private ChannelInboundHandlerAdapter somethingServerHandler;
	@Autowired
	private CustomizeProtocolEncoder customizeProtocolEncoder;

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
		pipeline.addLast(new IdleStateHandler(120, 120, 300));
		pipeline.addLast(new CustomizeBaseFrameDecoder((byte)0x7e));
		pipeline.addLast(new CustomizeProtocolDecoder());
		pipeline.addLast(customizeProtocolEncoder);
		pipeline.addLast(somethingServerHandler);
	}

}
