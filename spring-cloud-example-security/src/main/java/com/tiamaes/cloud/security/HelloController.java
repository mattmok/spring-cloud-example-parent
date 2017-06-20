package com.tiamaes.cloud.security;

import javax.servlet.http.HttpSession;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;

@RestController
@Secured("ADMIN")
public class HelloController {
	
	
	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public @ResponseBody String hello(HttpSession session, @CurrentUser User user) {
		return "Hello world, " +  user.getUsername() + "!!!";
	}
	
	@Secured("ADMIN")
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public @ResponseBody String admin(HttpSession session, @CurrentUser Authentication user) {
		return "Hello world, " + (user == null ? session.getId() : user.getPrincipal()) + "!";
	}
	
	@RequestMapping(value = "/legacy", method = RequestMethod.GET)
	public @ResponseBody String legacy() {
		return "Hello world, legacy !";
	}
}
