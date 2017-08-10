package com.tiamaes.cloud.eureka.server.service;

import java.util.List;

import com.tiamaes.cloud.eureka.server.bean.Server;
import com.tiamaes.mybatis.Pagination;

public interface ServerServiceInterface {

	public Server addServer(Server server);
	
	public Server updateServer(Server server);

	public boolean checkName(String name);

	public List<Server> getAllServers(Server server, Pagination<Server> pagination);
}
