package com.tiamaes.cloud.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class DefaultController {
	
	@RequestMapping(value = {"/{module:(?!module|resources).+}/**/*.html", "/build.html", "/monitor/index.html"}, method = RequestMethod.GET)
	public void module(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getRequestDispatcher("/index.html").forward(request, response);
	}
}
