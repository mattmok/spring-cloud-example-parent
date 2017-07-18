package com.tiamaes.cloud.security;

import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.security.core.annotation.CurrentUser;


@RestController
public class HelloController {
	
	
	@PreAuthorize("hasRole('DEVELOPER') OR hasRole('USER')")
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public @ResponseBody String hello(HttpSession session, @CurrentUser Authentication user) {
//		try {
//			Thread.sleep(10 * 1000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return "Hello world, " + (user == null ? session.getId() : user.getPrincipal()) + "!";
	}
}
