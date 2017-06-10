package com.tiamaes.cloud.netty;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiamaes.cloud.netty.protocol.handler.Handler;
import com.tiamaes.cloud.netty.protocol.handler.HandlerFactory;
import com.tiamaes.cloud.netty.protocol.message.Message;
import com.tiamaes.cloud.netty.protocol.message.Received;
import com.tiamaes.cloud.netty.repository.ChannelRepository;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@Component
@ChannelHandler.Sharable
@Qualifier("somethingServerHandler")
public class SomethingServerHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LogManager.getLogger(SomethingServerHandler.class);
	/**
	 * <p>
	 * 连接注册器，可该注册器中取出指定终端的连接
	 * </p>
	 * <code>
	 * 		Channel channel = channelRepository.get(UUIDGenerator.getUUID());
			channel.writeAndFlush(object);
	 * </code>
	 */
	@Autowired
	private ChannelRepository channelRepository;
	@Autowired
	private HandlerFactory handlerFactory;

	/**
	 * 连接建立成功之后触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
		super.channelActive(ctx);
		logger.trace("{} has been connected.", ctx.channel());
	}

	/**
	 * 处理消息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		Channel channel = ctx.channel();
		Received received = (Received) obj;
		Handler<Received> handler = handlerFactory.getHandler(received);
		Message response = handler.execute(channel, received);
		if (response != null) {
			channel.writeAndFlush(response);
		}
	}

	/**
	 * 产生异常时触发
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (!(cause instanceof IOException)) {
			logger.error(cause.getMessage(), cause);
		}
		logger.trace("process has been exceptionCaughted. Current Size : {}", channelRepository.size());
		ctx.close();
	}

	/**
	 * 当长连接断开时触发，在连接注册器中注销该终端
	 * 
	 * @throws JsonProcessingException
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws JsonProcessingException, UnknownHostException {
		Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		logger.trace("[{}] has been disconnected.", ctx.channel());
	}

	/**
	 * 超时处理
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) {
				logger.trace("{} has been disengaged. Current Size : {}", ctx.channel(), channelRepository.size());
				ctx.close();
			}
		}
	}
}