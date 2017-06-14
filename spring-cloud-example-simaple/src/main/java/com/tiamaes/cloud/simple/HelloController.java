package com.tiamaes.cloud.simple;

import javax.servlet.http.HttpSession;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.security.core.annotation.CurrentUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "hello")
public class HelloController {
	
	
	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@ApiOperation(value = "hello value", notes = "hello notes", response = String.class)
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public @ResponseBody String hello(HttpSession session, @CurrentUser Authentication user) {
//		try {
//			Thread.sleep(10 * 1000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return "Hello world, " + (user == null ? session.getId() : user.getPrincipal()) + "!";
	}
	
	@Secured("ADMIN")
	@ApiOperation(value = "hello value", notes = "hello notes", response = String.class)
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public @ResponseBody String admin(HttpSession session, @CurrentUser Authentication user) {
		return "Hello world, " + (user == null ? session.getId() : user.getPrincipal()) + "!";
	}
}
