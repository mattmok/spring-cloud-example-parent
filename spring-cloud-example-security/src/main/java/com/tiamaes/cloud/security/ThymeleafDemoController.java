package com.tiamaes.cloud.security;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ThymeleafDemoController {
	
	@RequestMapping(value = "/greeting", method = RequestMethod.GET)
	public String thymeleaf(Model model) {
		model.addAttribute("name", "boot");
		List<String> items = Arrays.asList(new String[]{"1","2","3"});
		model.addAttribute("items", items);
		model.addAttribute("date", new Date());
		return "hello";
	}
}
