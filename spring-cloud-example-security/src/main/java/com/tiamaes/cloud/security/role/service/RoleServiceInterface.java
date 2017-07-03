package com.tiamaes.cloud.security.role.service;

import java.util.List;

import com.tiamaes.cloud.security.role.bean.Role;
import com.tiamaes.cloud.security.role.bean.RoleAuthority;
import com.tiamaes.mybatis.Pagination;
import com.tiamaes.security.core.userdetails.User;

public interface RoleServiceInterface {
	
	List<Role> getAllRoles(User operator);
	/**
	 * 根据条件分页查询角色列表
	 * @param params
	 * @param page 
	 * @return
	 */
	List<Role> getAllRoles(Role params, Pagination<?> pagination);
	/**
	 * 修改角色
	 * 
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	Role updateRole(Role role);

	/**
	 * 增加角色
	 * 
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	Role saveRole(Role role);

	/**
	 * 根据角色编号查询角色信息
	 * 
	 * @param roleId
	 * @return
	 */
	Role getRoleByAuthority(String id);
	/**
	 * 根据id删除指定的角色
	 * 
	 * @param roleId
	 * @return
	 */
	void deleteRoleById(String roleId);
	/**
	 * 查询角色是否已经存在
	 * @param id
	 * @return
	 */
	boolean hasExists(String id);
	/**
	 * 查询角色权限
	 * @param id
	 * @return
	 */
	List<RoleAuthority> getAuthorization(String id);
	/**
	 * 保存角色权限
	 * @param id
	 * @param resourceid
	 */
	void saveAuthorization(String id, String resourceid);
	/**
	 * 删除角色权限
	 * @param id
	 * @param resourceid
	 */
	void deleteAuthorization(String id, String resourceid);
}
