package com.tiamaes.cloud;


import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application{
	private static Logger logger = LogManager.getLogger(Application.class);
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		if(logger.isTraceEnabled()){
			logger.trace("Let's inspect the beans provided by Spring Boot:\n");
			String[] array = context.getBeanDefinitionNames();
			Arrays.stream(array).forEach(bean -> logger.trace(bean));
		}
	}
	
	
//	@Bean
//	public AuthenticationSuccessHandler successHandler() {
//		return new AuthenticationSuccessHandler() {
//
//			@Override
//			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
//					throws IOException, ServletException {
//				//TODO your code
//			}
//		};
//	}
//	
//	@Bean
//	public AuthenticationFailureHandler failureHandler() {
//		return new AuthenticationFailureHandler() {
//
//			@Override
//			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//					AuthenticationException exception) throws IOException, ServletException {
//				// TODO your code
//			}
//
//		};
//	}
}