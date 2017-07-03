package com.tiamaes.cloud.security.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tiamaes.cloud.security.user.bean.MutableUser;
import com.tiamaes.mybatis.Pagination;

public interface UserServiceInterface extends UserDetailsService{
	/**
	 * 根据username查询用户详情
	 * @param username
	 * @return
	 */
	MutableUser getMutableUserByUsername(String username);
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
	MutableUser addMutableUser(MutableUser user);
	/**
	 * 更新用户
	 * @param user
	 * @throws Exception 
	 */
	MutableUser updateUser(MutableUser user);
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
}
