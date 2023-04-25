package com.haiersoft.ccli.system.sso;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
	@Override
	protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
		// 增加免密登录功能，使用自定义token
		return new CustomToken(username, password, LoginType.PASSWORD, rememberMe, host);
	}
}
