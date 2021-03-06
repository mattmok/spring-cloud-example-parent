package com.tiamaes.cloud.security;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("name", principal.getName());
		return map;
	}
	
	@RequestMapping(value = {"/{module:(?!module|resources).+}/**/*.html", "/build.html", "/monitor/index.html"}, method = RequestMethod.GET)
	public void module(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getRequestDispatcher("/index.html").forward(request, response);
	}
	
	@GetMapping("/module/**/*.html")
	public String module(HttpServletRequest request, @CurrentUser User operator, Model model){
		return StringUtils.removeStartIgnoreCase(request.getRequestURI(), request.getContextPath());
	}
	
	
	@RequestMapping(value = {"/index.html", "/"}, method = RequestMethod.GET)
	public String index(@CurrentUser User operator, Model model){
		if(StringUtils.isBlank(operator.getNickname())){
			operator.setNickname(operator.getUsername());
		}
		model.addAttribute("operator", operator);
		model.addAttribute("menus", resourceService.getNavigation(operator));
		return "index";
	}
}
