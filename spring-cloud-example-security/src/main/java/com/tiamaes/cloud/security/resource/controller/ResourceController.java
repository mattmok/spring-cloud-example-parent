package com.tiamaes.cloud.security.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.cloud.security.resource.bean.Resource;
import com.tiamaes.cloud.security.resource.service.ResourceServiceInterface;
import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;

@RestController
@Secured("ROLE_DEV")
@RequestMapping("/resource")
public class ResourceController {
	@Autowired
	private ResourceServiceInterface resourceService;

	@GetMapping("/list")
	public @ResponseBody List<Resource> list(@CurrentUser User operator) {
		List<Resource> resources = resourceService.getChildren();
		return resources;
	}

	@PostMapping("/page")
	public @ResponseBody List<Resource> page(@CurrentUser User operator) {
		List<Resource> resources = resourceService.getAllResources();
		return resources;
	}

	@PutMapping
	public @ResponseBody Resource add(@RequestBody Resource resource, @CurrentUser User operator) {
		resource = resourceService.addResource(resource);
		return resource;
	}

	@PutMapping("/child/{id}")
	public @ResponseBody Resource child(@RequestBody Resource resource, @PathVariable("id") String id) {
		resource.setParentId(id);
		return resourceService.addResource(resource);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody void delete(@PathVariable("id") String id) {
		resourceService.deleteResource(id);
	}

	@PutMapping("/{id}")
	public @ResponseBody Resource update(@RequestBody Resource resource, @PathVariable("id") String id) {
		resource.setId(id);
		return resourceService.updateResource(resource);
	}
}
