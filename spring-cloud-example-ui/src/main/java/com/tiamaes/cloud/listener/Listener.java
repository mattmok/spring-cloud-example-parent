package com.tiamaes.cloud.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Listener {
	private static Logger logger = LogManager.getLogger(Listener.class);

	@KafkaListener(id = "listen1", topics = "icms-fault-topic", containerFactory = "stringListenerContainerFactory")
	public void listen1(@Payload String payload) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("------------------Received vehicle : {}-------------------", payload);
		}
	}
}