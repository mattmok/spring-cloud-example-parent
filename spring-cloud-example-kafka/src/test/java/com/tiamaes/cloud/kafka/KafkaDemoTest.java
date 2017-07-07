package com.tiamaes.cloud.kafka;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaDemoTest {
	private static Logger logger = LoggerFactory.getLogger(KafkaDemoTest.class);
	
	@Autowired
	@Qualifier("kafkaTemplate")
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Resource
	private ObjectMapper jacksonObjectMapper;

	@Before
	public void before() {
		assertNotNull("KafkaTemplate<String, String> not found...", kafkaTemplate);
	}
	
	@Test
	public void test() throws Exception {
		Map<String, Object> event = new LinkedHashMap<String, Object>();
		event.put("busNo", "10000");
		event.put("type", 1);
		event.put("startTime", new Date());
		String payload = jacksonObjectMapper.writeValueAsString(event);
		
		logger.debug(payload);
		kafkaTemplate.send(MessageBuilder.withPayload(payload).setHeader(KafkaHeaders.TOPIC, "com.tm.longbus.event.domain.Event").build());
	}
}

