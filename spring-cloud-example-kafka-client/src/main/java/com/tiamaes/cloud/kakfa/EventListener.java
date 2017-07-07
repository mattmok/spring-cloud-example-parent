package com.tiamaes.cloud.kakfa;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class EventListener {
	private static Logger logger = LoggerFactory.getLogger(EventListener.class);
	
	@Resource
	private ObjectMapper jacksonObjectMapper;
	
	@KafkaListener(id = "listen1", topics = "com.tm.longbus.event.domain.Event")
	public void listen(@Payload String payload){
		logger.debug(payload.toString());
		
		try {
			Object object = jacksonObjectMapper.readValue(payload, Object.class);
			logger.debug("object : {}", object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
