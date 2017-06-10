package com.tiamaes.cloud.resource;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody Map<String,String> greeting(HttpSession session) {
		System.out.println(session.getId());
		Map<String, String> greeting = new HashMap<String, String>();
		greeting.put("id", session.getId());
		greeting.put("content", "Hello world!");
		return greeting;
	}
}
