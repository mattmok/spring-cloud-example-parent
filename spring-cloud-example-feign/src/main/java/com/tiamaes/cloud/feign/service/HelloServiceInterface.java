package com.tiamaes.cloud.feign.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "spring-cloud-example-simaple", path = "/simple")
public interface HelloServiceInterface {

	@RequestMapping(method = RequestMethod.GET, value = "/hello")
	public String hello();
}
