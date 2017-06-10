package com.tiamaes.cloud.netty.protocol.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tiamaes.cloud.netty.protocol.message.Received;

@Component
public class HandlerFactory {
	private static Logger logger = LogManager.getLogger(HandlerFactory.class);

	@Autowired
	private ApplicationContext context;
	
	@SuppressWarnings("unchecked")
	public Handler<Received> getHandler(Received received){
		String beanName = String.format("protocol%sHandler", String.format("%04x", received.getHeader().getId()));
		Handler<Received> handler = context.getBean(beanName, Handler.class);
		if (handler == null) {
			logger.error("No handler matched with " + beanName);
		}
		return handler;
	}
}
