package com.tiamaes.cloud.simple;

import org.jboss.logging.Logger;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

import com.tiamaes.cloud.redis.listener.RedisKeyExpiredListener;

@Component
public class RedisKeyExpiredMessageListener implements RedisKeyExpiredListener {
	private static Logger logger = Logger.getLogger(RedisKeyExpiredMessageListener.class);
	
	@Override
	public void onMessage(RedisKeyExpiredEvent<String> event) {
		logger.debug(new String(event.getSource()));
		logger.debug(new String(event.getId()));
		logger.debug(event.getKeyspace());
	}
}

