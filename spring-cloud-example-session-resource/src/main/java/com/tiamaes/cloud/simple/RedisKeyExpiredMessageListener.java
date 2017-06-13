package com.tiamaes.cloud.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

import com.tiamaes.cloud.redis.listener.RedisKeyExpiredListener;

@Component
public class RedisKeyExpiredMessageListener implements RedisKeyExpiredListener {
	private static Logger logger = LoggerFactory.getLogger(RedisKeyExpiredMessageListener.class);
	
	@Override
	public void onMessage(RedisKeyExpiredEvent<String> event) {
		logger.debug(new String(event.getSource()));
		logger.debug(new String(event.getId()));
		logger.debug(event.getKeyspace());
	}
}

