package com.tiamaes.cloud.gateway;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tiamaes.cloud.security.resource.service.ResourceServiceInterface;
import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;


@Controller
public class DefaultController {
	@Autowired
	private ResourceServiceInterface resourceService;
	
	@GetMapping("/module/**/*.html")
	public String module(HttpServletRequest request, @CurrentUser User operator, Model model){
		return StringUtils.removeStartIgnoreCase(request.getRequestURI(), request.getContextPath());
	}
	
	
	@RequestMapping(value = {"/index.html", "/"}, method = RequestMethod.GET)
	public String index(@CurrentUser User operator, Model model){
		model.addAttribute("operator", operator);
		model.addAttribute("menus", resourceService.getNavigation());
		return "index";
	}
}
