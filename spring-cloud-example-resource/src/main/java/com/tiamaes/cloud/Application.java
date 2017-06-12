package com.tiamaes.cloud;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class Application {

	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		if (logger.isTraceEnabled()) {
			logger.trace("Let's inspect the beans provided by Spring Boot:\n");
			String[] array = context.getBeanDefinitionNames();
			Arrays.stream(array).forEach(bean -> logger.trace(bean));
		}
	}
}
