package com.tiamaes.cloud.security.role.bean;

import java.io.Serializable;
import java.util.List;

public class RoleAuthority implements Serializable {
	private static final long serialVersionUID = -3774220178168399649L;

	private String id;
	private String name;

	private Boolean parent;
	private Boolean open;
	private Boolean checked;
	private List<RoleAuthority> children;

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

	public Boolean getParent() {
		if(this.parent == null){
			return children != null && children.size() > 0 ? true: false;
		}else{
			return this.parent;
		}
	}

	public void setParent(Boolean parent) {
		this.parent = parent;
	}

	public Boolean getOpen() {
		if(this.open == null){
			return children != null && children.size() > 0 ? true: false;
		}else{
			return this.open;
		}
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<RoleAuthority> getChildren() {
		return children;
	}

	public void setChildren(List<RoleAuthority> children) {
		this.children = children;
	}

}
