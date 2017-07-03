package com.tiamaes.cloud.security.role.bean;

import java.util.Date;

import com.tiamaes.cloud.security.organization.bean.Organization;
import com.tiamaes.security.core.DefaultGrantedAuthority;

public class Role extends DefaultGrantedAuthority {
	private static final long serialVersionUID = -9118721073078126986L;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 组织机构
	 */
	private Organization organization;

	public Role() {
	}

	public Role(String authority) {
		super(authority, authority);
	}

	public Role(String authority, String alias) {
		super(authority, alias);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
