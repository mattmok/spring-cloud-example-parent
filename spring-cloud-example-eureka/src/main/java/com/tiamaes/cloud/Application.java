package com.tiamaes.cloud;


import java.util.Arrays;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


@EnableEurekaServer
@EnableResourceServer
@SpringBootApplication
@MapperScan("com.tiamaes.cloud.**.persistence")
public class Application{
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		if(logger.isTraceEnabled()){
			logger.trace("Let's inspect the beans provided by Spring Boot:\n");
			String[] array = context.getBeanDefinitionNames();
			Arrays.stream(array).forEach(bean -> logger.trace(bean));
		}
	}
}