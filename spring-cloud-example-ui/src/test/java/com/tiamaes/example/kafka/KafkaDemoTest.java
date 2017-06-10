package com.tiamaes.example.kafka;

import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaDemoTest {
	private static Logger logger = LogManager.getLogger(KafkaDemoTest.class);
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Before
	public void before() {
		assertNotNull("KafkaTemplate<String, String> not found...", kafkaTemplate);
	}
	
	@Test
	public void test() throws Exception {
		String content = "hello";
		for(int i = 0; i < 10; i++){
			logger.debug("-------------------------------------");
			logger.debug(content + i);
			logger.debug("-------------------------------------");
			kafkaTemplate.send("command-0", 0, String.valueOf(i), content + i);
		}
	}

}

