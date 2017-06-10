package com.tiamaes.cloud;


import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableEurekaClient
@SpringBootApplication
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
public class Application{
	private static Logger logger = LogManager.getLogger(Application.class);
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		if(logger.isDebugEnabled()){
			logger.debug("Let's inspect the beans provided by Spring Boot:\n");
			String[] array = context.getBeanDefinitionNames();
			Arrays.stream(array).forEach(bean -> logger.debug(bean));
		}
	}
}