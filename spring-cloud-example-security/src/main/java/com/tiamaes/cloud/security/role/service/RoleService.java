package com.tiamaes.cloud.security.role.service;


import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tiamaes.cloud.security.resource.bean.Resource;
import com.tiamaes.cloud.security.resource.service.ResourceServiceInterface;
import com.tiamaes.cloud.security.role.bean.Role;
import com.tiamaes.cloud.security.role.bean.RoleAuthority;
import com.tiamaes.cloud.security.role.persistence.RoleMapper;
import com.tiamaes.cloud.util.Assert;
import com.tiamaes.mybatis.PageHelper;
import com.tiamaes.mybatis.Pagination;
import com.tiamaes.security.core.userdetails.User;

@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED)
public class RoleService implements RoleServiceInterface {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(RoleServiceInterface.class);
	
	private static final String PREFIX = "ROLE_";

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private ResourceServiceInterface resourceService;
	
	@Override
	public List<Role> getAllRoles(User operator) {
		return roleMapper.getAllRolesByParams(null);
	}
	
	@Override
	public List<Role> getAllRoles(Role params, Pagination<?> pagination) {
		PageHelper.startPage(pagination);
		return roleMapper.getAllRolesByParams(params);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Role updateRole(Role role) {
		Assert.notNull(role, "角色不能为空");
		Assert.notNull(role.getAuthority(), "角色名称不能为空");
		Assert.notNull(role.getAlias(), "角色别名不能为空");
		Role source = getRoleByAuthority(role.getAuthority());
		try {
			BeanUtils.copyProperties(role, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		roleMapper.updateRole(source);
		return source;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Role saveRole(Role role){
		Assert.notNull(role, "角色不能为空");
		Assert.notNull(role.getAuthority(), "角色名称不能为空");
		Assert.notNull(role.getAlias(), "角色别名不能为空");
		role.setCreateDate(new Date());
		String authority = role.getAuthority().toUpperCase();
		if(!authority.startsWith(PREFIX)){
			authority = PREFIX + authority;
		}
		role.setAuthority(authority);
		roleMapper.insertRole(role);
		role = getRoleByAuthority(role.getAuthority());
		return role;
	}
	

	@Override
	public Role getRoleByAuthority(String authority) {
		Assert.notNull(authority, "角色编号不能为空");
		return roleMapper.getRoleByAuthority(authority);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteRoleById(String authority){
		Role role = getRoleByAuthority(authority);
		Assert.notNull(authority, "角色不存在或已经被删除");
		if (role != null) {
			roleMapper.deleteResourceRoleByAuthority(authority);
			roleMapper.deleteUserRoleByAuthority(authority);
			roleMapper.deleteRoleByAuthority(authority);
		}
	}

	@Override
	public boolean hasExists(String authority) {
		Assert.notNull(authority, "角色编号不能为空");
		return roleMapper.hasExists(fixedAuthority(authority));
	}

	@Override
	public List<RoleAuthority> getAuthorization(String authority) {
		Assert.notNull(authority, "角色编号不能为空");
		return roleMapper.getAuthorization(fixedAuthority(authority));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAuthorization(String authority, String resourceid) {
		Assert.notNull(authority, "角色编号不能为空");
		Assert.notNull(resourceid, "授权资源不能为空");
		authority = fixedAuthority(authority);
		Resource resource = resourceService.getResourceById(resourceid);
		List<Resource> children = resourceService.getChildren(resourceid);
		children.add(resource);
		if(resource.getParentId() != null){
			List<Resource> parents = resourceService.getParents(resource.getParentId());
			children.addAll(parents);
		}
		for(Resource child : children){
			if(!roleMapper.hasAuthorized(authority, child.getId())){
				roleMapper.saveAuthorization(authority, child.getId());
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteAuthorization(String authority, String resourceid) {
		Assert.notNull(authority, "角色编号不能为空");
		Assert.notNull(resourceid, "授权资源不能为空");
		List<Resource> children = resourceService.getChildren(resourceid);
		Resource resource = resourceService.getResourceById(resourceid);
		children.add(resource);
		for(Resource child : children){
			roleMapper.deleteAuthorization(fixedAuthority(authority), child.getId());
		}
		
	}
	
	private String fixedAuthority(String authority) {
		StringBuilder builder = new StringBuilder();
		builder.append(PREFIX);
		builder.append(authority);
		return builder.toString();
	}
}
