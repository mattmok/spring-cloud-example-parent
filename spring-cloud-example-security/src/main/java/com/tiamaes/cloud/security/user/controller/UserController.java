package com.tiamaes.cloud.security.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiamaes.cloud.security.user.bean.MutableUser;
import com.tiamaes.cloud.security.user.service.UserServiceInterface;
import com.tiamaes.mybatis.PageInfo;
import com.tiamaes.mybatis.Pagination;
import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;

@RestController
@RequestMapping("/user")
public class UserController {
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(UserController.class);
	@Resource
	private UserServiceInterface userService;
	
	@RequestMapping(value = {"/page/{number:\\d+}","/page/{number:\\d+}/{pageSize:\\d+}"}, method = RequestMethod.POST)
	public @ResponseBody PageInfo<MutableUser> page(@RequestBody MutableUser mutableUser, @PathVariable Map<String,String> pathVariable, @CurrentUser User operator) {
		int number = pathVariable.get("number") == null ? 1 : Integer.parseInt(pathVariable.get("number"));
		int pageSize = pathVariable.get("pageSize") == null ? 20 : Integer.parseInt(pathVariable.get("pageSize"));
		Pagination<User> pagination = new Pagination<User>(number, pageSize);
		List<MutableUser> list = userService.getAllMutableUsers(mutableUser, pagination);
		return new PageInfo<MutableUser>(list);
	}
	
	@RequestMapping(value = "/checkname/{username}", method = RequestMethod.GET)
	public @ResponseBody Map<String,Boolean> checkname(@PathVariable("username")String username) {
		Map<String,Boolean> result = new HashMap<String,Boolean>();
		Boolean state = userService.checkUserName(username);
		result.put("state", state);
		return result;
	}
	
	@Secured("ADMIN")
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	public @ResponseBody void delete(@PathVariable("username")String username, @CurrentUser com.tiamaes.security.core.userdetails.User operator) {
		if(StringUtils.isNotBlank(username)){
			userService.deleteUser(username);
		}
	}
	
//	@RequestMapping(method = RequestMethod.PUT)
//	public @ResponseBody User add(@RequestBody User user, @CurrentUser com.tiamaes.security.core.userdetails.User operator){
//		user = userService.addUser(user);
//		return user;
//	}
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public @ResponseBody User register(@RequestBody User user){
//		return userService.register(user);
//	}
//	
//	@RequestMapping(value = "/updatepwd/{username}/{newpasswd}", method = RequestMethod.PUT)
//	public @ResponseBody void updatePwd(@PathVariable String username, @PathVariable String newpasswd) {
//		User actual = userService.getUserById(username);
//		MutableUser mutableUser = new MutableUser(actual);
//		mutableUser.setPassword(passwordEncoder.encode(newpasswd));
//		userService.upadatePassword(mutableUser);
//	}
}
