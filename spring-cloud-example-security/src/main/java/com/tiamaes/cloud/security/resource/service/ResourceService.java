package com.tiamaes.cloud.security.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tiamaes.cloud.security.resource.bean.Resource;
import com.tiamaes.cloud.security.resource.persistence.ResourceMapper;
import com.tiamaes.cloud.security.role.persistence.RoleMapper;
import com.tiamaes.cloud.util.Assert;
import com.tiamaes.cloud.util.UUIDGenerator;
import com.tiamaes.security.core.userdetails.User;



/**
 * @author Chen
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ResourceService implements ResourceServiceInterface {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ResourceServiceInterface.class);
	
	@javax.annotation.Resource
	private ResourceMapper resourceMapper;
	@javax.annotation.Resource
	private RoleMapper roleMapper;
	
	@Override
	public List<Resource> getAllResources() {
		return resourceMapper.getAllResources();
	}
	
	@Override
	public List<Resource> getChildren() {
		return getChildren(null);
	}
	
	@Override
	public List<Resource> getChildren(String id) {
		List<Resource> resources = resourceMapper.getChildren(id);
		if(resources != null && resources.size() > 0){
			for(Resource resource : resources){
				List<Resource> children = getChildren(resource.getId());
				if(children != null && children.size() > 0){
					resource.setChildren(children);
				}
			}
		}
		return resources;
	}
	
	@Override
	public List<Resource> getParents(String id) {
		List<Resource> list = new ArrayList<Resource>();
		Resource resource = resourceMapper.getResourceById(id);
		list.add(resource);
		if(resource != null && resource.getParentId() != null){
			list.addAll(getParents(resource.getParentId()));
		}
		return list;
	}
	
	@Override
	public List<Resource> getNavigation(User user) {
		Assert.notNull(user, "parameter 'user' cannot be empty or null");
		List<Resource> resources = resourceMapper.getNavigation(user.getUsername());
		return resources;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public Resource addResource(Resource resource) {
		Assert.notNull(resource, "资源不能为空");
		Assert.notNull(resource.getName(), "资源名称不能为空");
		final String id = UUIDGenerator.getUUID();
		resource.setId(id);
		if(StringUtils.isBlank(resource.getParentId())){
			resource.setRank(0);
			resource.setParentId(null);
		}else{
			Resource parent = getResourceById(resource.getParentId());
			Assert.notNull(parent, "无效的上级资源编号");
			resource.setRank(parent.getRank() + 1);
		}
		
		if(StringUtils.isBlank(resource.getPath())){
			resource.setPath(null);
		}
		if(StringUtils.isBlank(resource.getIco())){
			resource.setIco(null);
		}
		resourceMapper.addResource(resource);
		roleMapper.saveAuthorization("ROLE_ADMIN", resource.getId());
		roleMapper.saveAuthorization("ROLE_DEV", resource.getId());
		return resource = resourceMapper.getResourceById(id);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public void deleteResource(String id){
		Assert.notNull(id, "要删除的资源编号不能为空");
		Resource resource = getResourceById(id);
		Assert.notNull(resource, "要删除的资源不存在或已经被删除");
		
		Assert.isTrue(resourceMapper.hasChildren(id) == false, "要删除的资源包含下级资源，不能删除!");
		
		resourceMapper.deleteResource(id);
		resourceMapper.deleteRolesResource(id);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public Resource updateResource(Resource resource){
		Assert.notNull(resource, "资源不能为空");
		Assert.notNull(resource.getId(), "资源编号不能为空");
		Assert.notNull(resource.getName(), "资源名称不能为空");
		Resource source = getResourceById(resource.getId());
		try {
			BeanUtils.copyProperties(resource, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resourceMapper.updateResource(resource);
		return resourceMapper.getResourceById(resource.getId());
	}
	
	@Override
	public Resource getResourceById(String id) {
		Assert.notNull(id, "资源编号不能为空");
		Resource resource = resourceMapper.getResourceById(id);
		return resource;
	}
}
