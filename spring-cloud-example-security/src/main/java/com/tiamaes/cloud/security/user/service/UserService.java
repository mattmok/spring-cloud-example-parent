package com.tiamaes.cloud.security.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tiamaes.cloud.security.user.bean.MutableUser;
import com.tiamaes.cloud.security.user.persistence.UserMapper;
import com.tiamaes.cloud.util.Assert;
import com.tiamaes.mybatis.PageHelper;
import com.tiamaes.mybatis.Pagination;
import com.tiamaes.security.core.userdetails.User;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserService implements UserServiceInterface {
	private static Logger logger = LogManager.getLogger(UserService.class);

	@Resource
	private UserMapper userMapper;
	@Resource
	private PasswordEncoder passwordEncoder;
	@Resource
	private UserServiceInterface userService;

	@Override
	public boolean checkUserName(String username) {
		return userMapper.hasExists(username);
	}

	@Override
	public boolean hasRole(String authority) {
		return userMapper.hasRole(authority);
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		Assert.notNull(username, "用户名不能为空!");
		User user = userMapper.loadUserDetailByUsername(username);
		if (user != null) {
			return user;
		}
		return null;
	}

	@Override
	public MutableUser getMutableUserByUsername(String username) {
		Assert.notNull(username, "用户名称不能为空!");
		MutableUser user = userMapper.getMutableUserById(username);
		return user;
	}

	@Override
	public List<MutableUser> getAllMutableUsers(MutableUser mutableUser, Pagination<?> pagination) {
		PageHelper.startPage(pagination);
		return userMapper.getAllMutableUsers(mutableUser);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public MutableUser addMutableUser(MutableUser user) {
		Assert.notNull(user, "user not be null");
		Assert.isTrue(!userMapper.hasExists(user.getUsername()), "用户名已经存在!");
		String password = passwordEncoder.encode("123456");
		user.setPassword(password);
		userMapper.insertMutableUser(user);
		if (user.getAuthorities() != null && user.getAuthorities().size() > 0) {
			userMapper.addUserRoles(user);
		}
		return userMapper.getMutableUserById(user.getUsername());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public MutableUser updateUser(MutableUser user) {
		Assert.notNull(user, "user not be null");
		Assert.notNull(user.getUsername(), "用户名称不能为空");
		MutableUser source = userMapper.getMutableUserById(user.getUsername());
		try {
			BeanUtils.copyProperties(user, source);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		userMapper.updateMutableUser(source);
		userMapper.deleteUserRoles(source);
		if (source.getAuthorities() != null && source.getAuthorities().size() > 0) {
			userMapper.addUserRoles(source);
		}
		return source;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteUser(String username) {
		Assert.notNull(username, "用户名称不能为空");
		MutableUser user = userMapper.getMutableUserById(username);
		Assert.notNull(user, "用户不能为空!");
		userMapper.deleteUser(user);
		userMapper.deleteUserRoles(user);
	}
}
