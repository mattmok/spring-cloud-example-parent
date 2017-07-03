package com.tiamaes.cloud.security.user.bean;

import java.util.Date;
import java.util.List;

import com.tiamaes.security.core.DefaultGrantedAuthority;

public class MutableUser {
	private String username;
	private String password;
	private String nickname;
	private String mobile;
	private String email;
	private Date createDate;

	private List<DefaultGrantedAuthority> authorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<DefaultGrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<DefaultGrantedAuthority> authorities) {
		this.authorities = authorities;
	}
}
