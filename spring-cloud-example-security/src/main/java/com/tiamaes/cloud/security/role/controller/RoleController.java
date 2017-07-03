package com.tiamaes.cloud.security.role.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.tiamaes.cloud.security.role.bean.Role;
import com.tiamaes.cloud.security.role.bean.RoleAuthority;
import com.tiamaes.cloud.security.role.service.RoleServiceInterface;
import com.tiamaes.mybatis.PageInfo;
import com.tiamaes.mybatis.Pagination;
import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/role")
public class RoleController {
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(RoleController.class);
	@Autowired
	private RoleServiceInterface roleService;
	
	
	@Secured({"ROLE_ADMIN", "ROLE_DEV"})
	@GetMapping("/list")
	public @ResponseBody List<Role> list(@CurrentUser User operator){
		return roleService.getAllRoles(operator);
	}

	@PostMapping({ "/page/{number:\\d+}", "/page/{number:\\d+}/{pageSize:\\d+}" })
	public @ResponseBody PageInfo<Role> page(@RequestBody Role parameters, @PathVariable Map<String, String> pathVariable, @CurrentUser User operator) {
		int number = pathVariable.get("number") == null ? 1 : Integer.parseInt(pathVariable.get("number"));
		int pageSize = pathVariable.get("pageSize") == null ? 20 : Integer.parseInt(pathVariable.get("pageSize"));
		Pagination<Role> pagination = new Pagination<Role>(number, pageSize);
		List<Role> roles = roleService.getAllRoles(parameters, pagination);
		return new PageInfo<Role>(roles);
	}
	
	@GetMapping("/check/{id}")
	public @ResponseBody Object saveRole(@PathVariable("id") String id, @CurrentUser User operator) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", roleService.hasExists(id));
		return result;
	}

	@PutMapping
	public @ResponseBody Role saveRole(@RequestBody Role role, @CurrentUser User operator) {
		return roleService.saveRole(role);
	}

	@PutMapping("/{id}")
	public @ResponseBody Role updateRole(@RequestBody Role role, @PathVariable("id") String id, @CurrentUser User operator) {
		return roleService.updateRole(role);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody void deleteRole(@CurrentUser User operator, @PathVariable("id") String id) {
		if(StringUtils.isNotBlank(id)){
			roleService.deleteRoleById(id);
		}
	}
	
	@GetMapping("/auth/{id}")
	public @ResponseBody List<RoleAuthority> auth(@PathVariable("id") String id) {
		List<RoleAuthority> auths = roleService.getAuthorization(id);
		return auths;
	}

	@PutMapping("/auth/{id}/{resourceid}")
	public @ResponseBody void saveAuth(@PathVariable("id") String id, @PathVariable("resourceid") String resourceid, @CurrentUser User operator) {
		roleService.saveAuthorization(id, resourceid);
	}

	@DeleteMapping("/auth/{id}/{resourceid}")
	public @ResponseBody void deleteAuth(@PathVariable("id") String id, @PathVariable("resourceid") String resourceid, @CurrentUser User operator) {
		roleService.deleteAuthorization(id, resourceid);
	}
}
