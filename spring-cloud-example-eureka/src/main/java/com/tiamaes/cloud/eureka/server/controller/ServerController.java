package com.tiamaes.cloud.eureka.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.cloud.eureka.server.bean.Server;
import com.tiamaes.cloud.eureka.server.service.ServerServiceInterface;

@RestController
@RequestMapping("/discovery/server")
public class ServerController {
	
	@Autowired
	private ServerServiceInterface serverService;
	
	@PutMapping
	public Server addServer(@RequestBody Server server){
		return serverService.addServer(server);
	}
	
	@PutMapping("/{id}")
	public Server updateServer(@RequestBody Server server, @PathVariable("id") String id){
		return serverService.updateServer(server);
	}
	
	@RequestMapping(value = "/check/{name}", method = RequestMethod.GET)
	public Object check(@PathVariable("name")String name){
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		boolean state = serverService.checkName(name);
		result.put("state", state);
		return result;
	}

}
