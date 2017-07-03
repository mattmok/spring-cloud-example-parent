package com.tiamaes.cloud.security.resource.bean;

import java.io.Serializable;
import java.util.Set;

import org.springframework.security.access.ConfigAttribute;

public class SecurityConfigs implements Serializable {
	private static final long serialVersionUID = -1525696651983420520L;

	private String id;

	private String path;

	private Set<ConfigAttribute> securityConfigs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSecurityConfigs(Set<ConfigAttribute> securityConfigs) {
		this.securityConfigs = securityConfigs;
	}

	public Set<ConfigAttribute> getSecurityConfigs() {
		return this.securityConfigs;
	}

}
