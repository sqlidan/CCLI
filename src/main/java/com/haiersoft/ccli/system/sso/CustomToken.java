package com.haiersoft.ccli.system.sso;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -647755036211432658L;
	private LoginType type;

	public CustomToken() {
		super();
	}

	public CustomToken(String username, String password, LoginType type, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		this.type = type;
	}

	/** 免密登录 */
	public CustomToken(String username) {
		super(username, "", false, null);
		this.type = LoginType.NOPASSWD;
	}

	/** 账号密码登录 */
	public CustomToken(String username, String password) {
		super(username, password, false, null);
		this.type = LoginType.PASSWORD;
	}

	public LoginType getType() {
		return type;
	}

	public void setType(LoginType type) {
		this.type = type;
	}

}
