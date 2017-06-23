package com.tiamaes.cloud.security.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tiamaes.cloud.security.user.bean.MutableUser;
import com.tiamaes.mybatis.Pagination;
import com.tiamaes.security.core.userdetails.User;

public interface UserServiceInterface extends UserDetailsService{
	/**
	 * 根据Id查询用户详情
	 * @param id
	 * @return
	 */
	User getUserById(String id);
	/**
	 * 查询注册用户列表
	 * @param user
	 * @param pagination
	 * @return
	 */
	List<MutableUser> getAllMutableUsers(MutableUser mutableUser, Pagination<?> pagination);
	/**
	 * 保存用户
	 * @param user
	 * @throws Exception 
	 */
	User addUser(User user);
	/**
	 * 更新用户
	 * @param user
	 * @throws Exception 
	 */
	User updateUser(User user);
	/**
	 * 删除用户
	 * @param username
	 */
	void deleteUser(String username);
	/**
	 * 检查用户名是否已经存在
	 * @param username
	 * @return
	 */
	boolean checkUserName(String username);
	/**
	 * 根据角色判断有没有该用户的角色
	 * @param authority
	 * @return
	 */
	boolean hasRole(String authority);
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	User register(User user);
}
