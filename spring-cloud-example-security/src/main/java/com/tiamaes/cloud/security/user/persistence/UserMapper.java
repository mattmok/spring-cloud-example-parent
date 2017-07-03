package com.tiamaes.cloud.security.user.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tiamaes.cloud.security.user.bean.MutableUser;
import com.tiamaes.security.core.userdetails.User;

/**
 * 用户数据持久层操作接口
 * 
 * @author chen
 *
 */
public interface UserMapper {

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	User loadUserDetailByUsername(@Param("username") String username);

	/**
	 * 根据用户名称查询用户信息
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	MutableUser getMutableUserById(@Param("username") String username);
	/**
	 * 查询所有用户
	 * 
	 * @return
	 */
	List<MutableUser> getAllMutableUsers(MutableUser user);
	/**
	 * 保存用户
	 * 
	 * @param user
	 */
	void insertMutableUser(MutableUser user);
	/**
	 * 保存用户角色
	 * 
	 * @param authorities
	 */
	void addUserRoles(MutableUser user);

	/**
	 * 更新用户
	 * 
	 * @param user
	 */
	void updateMutableUser(MutableUser user);
	/**
	 * 修改指定用户的密码
	 * 
	 * @param id
	 * @param newpwd
	 */
	void updatePassword(MutableUser user);

	/**
	 * 删除用户
	 * 
	 * @param user
	 */
	void deleteUser(MutableUser user);

	/**
	 * 删除用户关联角色
	 * 
	 * @param user
	 */
	void deleteUserRoles(MutableUser user);

	/**
	 * 删除用户设置
	 * 
	 * @param user
	 */
	void deleteUserSettings(User user);

	/**
	 * 是否有用户设置
	 * 
	 * @param source
	 * @return
	 */
	boolean existsUserInfo(User user);

	/**
	 * 检查用户名是否已经存在
	 * 
	 * @param username
	 * @return
	 */
	boolean hasExists(String username);

	/**
	 * 根据角色判断有没有该用户的角色
	 * 
	 * @param authority
	 * @return
	 */
	boolean hasRole(String authority);
}
