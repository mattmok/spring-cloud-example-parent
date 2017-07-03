package com.tiamaes.cloud.security.resource.service;

import java.util.List;

import com.tiamaes.cloud.security.resource.bean.Resource;
import com.tiamaes.security.core.userdetails.User;

/**
 * @author Chen
 *
 */
public interface ResourceServiceInterface {
	/**
	 * 查询资源列表
	 * 
	 * @return
	 */
	List<Resource> getAllResources();

	/**
	 * 获取系统资源
	 * 
	 * @return
	 */
	List<Resource> getChildren();

	/**
	 * 获取系统资源
	 * 
	 * @return
	 */
	List<Resource> getChildren(String id);

	/**
	 * 获取系统资源
	 * 
	 * @return
	 */
	List<Resource> getParents(String id);

	/**
	 * 根据用户角色获取用户门户菜单
	 * 
	 * @param user
	 * @return
	 */
	List<Resource> getNavigation(User user);

	/**
	 * 根据编号查询资源信息
	 * 
	 * @param id
	 * @return
	 */
	Resource getResourceById(String id);

	/**
	 * 创建新的资源
	 */
	Resource addResource(Resource resource);

	/**
	 * 删除资源
	 * 
	 * @param id
	 * @throws Exception
	 */
	void deleteResource(String id);

	/**
	 * 更新资源
	 * 
	 * @param resource
	 * @throws Exception
	 */
	Resource updateResource(Resource resource);
}
