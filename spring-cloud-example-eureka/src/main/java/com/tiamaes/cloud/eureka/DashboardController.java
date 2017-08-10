/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tiamaes.cloud.eureka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContext;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.tiamaes.cloud.eureka.server.bean.Server;
import com.tiamaes.cloud.eureka.server.service.ServerServiceInterface;
import com.tiamaes.mybatis.PageInfo;
import com.tiamaes.mybatis.Pagination;
import com.tiamaes.security.core.annotation.CurrentUser;
import com.tiamaes.security.core.userdetails.User;

@RestController
@RequestMapping("/discovery/instances")
public class DashboardController {
	
	@Autowired
	private ServerServiceInterface serverService;
	
	@PostMapping(value = {"/page/{number:\\d+}","/page/{number:\\d+}/{pageSize:\\d+}"})
	public PageInfo<Server> page(@PathVariable Map<String,String> pathVariable, @RequestBody Server server, @CurrentUser User operator){
		int number = pathVariable.get("number") == null ? 1 : Integer.parseInt(pathVariable.get("number"));
		int pageSize = pathVariable.get("pageSize") == null ? 20 : Integer.parseInt(pathVariable.get("pageSize"));
		Pagination<Server> pagination = new Pagination<Server>(number, pageSize);
		List<Server> list = serverService.getAllServers(server, pagination);
		List<Application> apps = getRegistry().getSortedApplications();
		Map<String, List<InstanceInfo>> caches = new HashMap<>();
		for(Application app: apps){
			caches.put(app.getName().toLowerCase(), app.getInstances());
		}
		for(Server sv : list){
			sv.setInstances(caches.get(sv.getName()));
		}
		return new PageInfo<Server>(list);
	}

	private PeerAwareInstanceRegistry getRegistry() {
		return getServerContext().getRegistry();
	}
	
	private EurekaServerContext getServerContext() {
		return EurekaServerContextHolder.getInstance().getServerContext();
	}
}
