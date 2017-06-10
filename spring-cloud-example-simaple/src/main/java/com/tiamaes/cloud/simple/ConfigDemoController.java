package com.tiamaes.cloud.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.cloud.config.DemoProperties;
import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;

@RestController
public class ConfigDemoController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private DemoProperties demoProperties;
	
	@RequestMapping(value = "/config/refresh", method = RequestMethod.GET)
	public @ResponseBody String refresh(@CurrentUser User user) {
		String name = demoProperties.getName();
		String actived = environment.getProperty("spring.profiles.active");
		return "I'm loaded [spring-cloud-example-simaple-" + actived + ".yml], demo.name = " + name + "!";
	}
}
