package com.tiamaes.cloud.netty.protocol.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tiamaes.cloud.netty.protocol.message.Message;
import com.tiamaes.cloud.netty.protocol.message.Message8001;
import com.tiamaes.cloud.netty.protocol.message.Received0001;

import io.netty.channel.Channel;

/**
 * 终端通用应答0x0001
 * 
 * @author Chen
 *
 */
@Component
public class Protocol0001Handler implements Handler<Received0001> {
	private static Logger logger = LogManager.getLogger(Protocol0001Handler.class);

	@Override
	public Message execute(Channel channel, Received0001 received) throws Exception {
		logger.debug(received.getClass() + ": " + received);
		
		Message message = new Message8001(received.getHeader());
		return message;
	}

}
