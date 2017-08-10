package com.tiamaes.cloud.eureka.server.bean;

import java.io.Serializable;
import java.util.List;

import com.netflix.appinfo.InstanceInfo;
import com.tiamaes.security.core.userdetails.User;

public class Server implements Serializable {
	private static final long serialVersionUID = -2350765213182829151L;

	private String id;
	private String name;
	private String svn;
	private String version;
	private User principal;

	private List<InstanceInfo> instances;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public User getPrincipal() {
		return principal;
	}

	public void setPrincipal(User principal) {
		this.principal = principal;
	}

	public List<InstanceInfo> getInstances() {
		return instances;
	}

	public void setInstances(List<InstanceInfo> instances) {
		this.instances = instances;
	}
}
