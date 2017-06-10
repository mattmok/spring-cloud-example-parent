package com.tiamaes.cloud.simple;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;

@RestController
public class HelloController {
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public @ResponseBody String hello(HttpSession session, @CurrentUser User user) {
		return "Hello world, " + (user == null ? session.getId() : user.getUsername()) + "!";
	}
}
