package com.tiamaes.cloud.feign.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.cloud.feign.service.HelloServiceInterface;
import com.tiamaes.security.core.annotation.CurrentUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "hello")
public class HelloController {
	
	@Autowired
	private HelloServiceInterface service;

	@ApiOperation(value = "hello value", notes = "hello notes", response = String.class)
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public @ResponseBody String hello(HttpSession session, @CurrentUser Authentication user) {
		return service.hello();
	}
}
