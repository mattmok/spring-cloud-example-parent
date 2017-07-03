package com.tiamaes.cloud.security.role.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tiamaes.cloud.security.role.bean.Role;
import com.tiamaes.cloud.security.role.bean.RoleAuthority;

public interface RoleMapper {
	/**
	 * 根据条件分页查询用户角色记录
	 * 
	 * @param map
	 * @param rowBounds
	 * @return
	 */
	List<Role> getAllRolesByParams(Role params);
	/**
	 * 根据id查询角色信息
	 * @param id
	 * @return
	 */
	Role getRoleByAuthority(String authority);
	/**
	 * 保存新角色
	 * @param role
	 */
	void insertRole(Role role);
	/**
	 * 更新角色信息
	 * @param role
	 */
	void updateRole(Role role);
	/**
	 * 根据id删除指定的角色信息
	 * @param id
	 */
	void deleteRoleByAuthority(String authority);
	/**
	 * 根据id删除角色相关的用户关系
	 * @param id
	 */
	void deleteUserRoleByAuthority(String authority);
	/**
	 * 根据id删除角色相关的资源关系
	 * @param id
	 */
	void deleteResourceRoleByAuthority(String authority);
	/**
	 * 根据authority查询是否可用
	 * @param authority
	 * @return
	 */
	boolean hasExists(String authority);
	/**
	 * 查询角色权限
	 * @param id
	 * @return
	 */
	List<RoleAuthority> getAuthorization(String authority);
	/**
	 * 判断是否已经授权
	 * @param authority
	 * @param id
	 * @return
	 */
	boolean hasAuthorized(@Param("authority")String authority, @Param("resourceid")String resourceid);
	void saveAuthorization(@Param("authority")String authority, @Param("resourceid")String resourceid);
	void deleteAuthorization(@Param("authority")String authority, @Param("resourceid")String resourceid);
}
