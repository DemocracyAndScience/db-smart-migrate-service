package com.system.entity;

import javax.validation.constraints.NotNull;

public class User {

	private Integer id ; 
	@NotNull(message="用户名不能为空")
	private String username ; 
	@NotNull(message="密码不能为空")
	private String password ;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	
	
	
}
