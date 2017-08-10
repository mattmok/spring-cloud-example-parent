package com.tiamaes.cloud.eureka.server.persistence;

import java.util.List;

import com.tiamaes.cloud.eureka.server.bean.Server;
import com.tiamaes.mybatis.Pagination;

public interface ServerMapper {

	Server queryServerById(String id);
	
	void insertServer(Server server);

	void updateServer(Server server);

	boolean queryExistsServerName(String name);

	List<Server> queryAllServers(Server server, Pagination<Server> pagination);
}
