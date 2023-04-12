package com.haiersoft.ccli.system.sso;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.haiersoft.ccli.common.utils.security.Encodes;
import com.haiersoft.ccli.system.entity.Permission;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.entity.UserRole;
import com.haiersoft.ccli.system.service.PermissionService;
import com.haiersoft.ccli.system.service.UserService;
import com.haiersoft.ccli.system.service.UserRealm.ShiroUser;

public class SsoUserRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();

		User user = userService.getUser(shiroUser.loginName);

		// 把principals放session中 key=userId value=principals
		SecurityUtils.getSubject().getSession().setAttribute(String.valueOf(user.getId()),
				SecurityUtils.getSubject().getPrincipals());

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		// 赋予角色
		for (UserRole userRole : user.getUserRoles()) {
			info.addRole(userRole.getRole().getName());
		}

		// 赋予权限
		for (Permission permission : permissionService.getPermissions(user.getId())) {
			if (StringUtils.isNotBlank(permission.getPermCode()))
				info.addStringPermission(permission.getPermCode());
		}

		// 设置登录次数、时间
		userService.updateUserLogin(user);
		return info;

	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		System.out.println("Shiro开始登录认证");

		// 增加免密登录功能，使用自定义token
		CustomToken token = (CustomToken) authcToken;

		User user = userService.getUser(token.getUsername());

		if (user == null) {
			throw new UnknownAccountException();
		}

		byte[] salt = Encodes.decodeHex(user.getSalt());
		ShiroUser shiroUser = new ShiroUser(user.getId(), user.getLoginName(), user.getName());

		// 设置用户session
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("user", user);

		return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), ByteSource.Util.bytes(salt), getName());
		// return null;
	}

}
