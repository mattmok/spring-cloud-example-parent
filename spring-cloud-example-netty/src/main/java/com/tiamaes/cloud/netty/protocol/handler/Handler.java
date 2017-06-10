package com.tiamaes.cloud.netty.protocol.handler;


import com.tiamaes.cloud.netty.protocol.message.Message;
import com.tiamaes.cloud.netty.protocol.message.Received;

import io.netty.channel.Channel;

public interface Handler<T extends Received> {

	public Message execute(Channel channel, T received) throws Exception;

}
