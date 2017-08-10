package com.tiamaes.cloud.eureka.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tiamaes.cloud.eureka.server.bean.Server;
import com.tiamaes.cloud.eureka.server.persistence.ServerMapper;
import com.tiamaes.cloud.util.Assert;
import com.tiamaes.cloud.util.UUIDGenerator;
import com.tiamaes.mybatis.Pagination;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ServerService implements ServerServiceInterface {

	@Autowired
	private ServerMapper serverMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Server addServer(Server server) {
		Assert.notNull(server.getName(), "服务名称不能为空");
		Assert.notNull(server.getSvn(), "服务SVN地址不能为空");
		Assert.notNull(server.getVersion(), "服务版本号不能为空");
		Assert.notNull(server.getPrincipal(), "服务负责人不能为空");
		server.setId(UUIDGenerator.getUUID());
		serverMapper.insertServer(server);
		return serverMapper.queryServerById(server.getId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Server updateServer(Server server) {
		Assert.notNull(server.getId(), "服务编号不能为空");
		Assert.notNull(server.getName(), "服务名称不能为空");
		Assert.notNull(server.getSvn(), "服务SVN地址不能为空");
		Assert.notNull(server.getVersion(), "服务版本号不能为空");
		Assert.notNull(server.getPrincipal(), "服务负责人不能为空");
		serverMapper.updateServer(server);
		return serverMapper.queryServerById(server.getId());
	}

	@Override
	public boolean checkName(String name) {
		return serverMapper.queryExistsServerName(name);
	}

	@Override
	public List<Server> getAllServers(Server server, Pagination<Server> pagination) {
		return serverMapper.queryAllServers(server, pagination);
	}

}
