package com.tiamaes.cloud.security.resource.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "resource", url = "http://127.0.0.1:8000/uaa")
public interface ResourceServiceInterface {

	@RequestMapping(method = RequestMethod.GET, value = "/resource/navigation")
	public List<Object> getNavigation();
}
