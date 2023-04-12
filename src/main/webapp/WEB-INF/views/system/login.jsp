<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	request.setAttribute("error", error);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>山东港口陆海国际物流集团有限公司</title>
<link rel="shortcut icon" href="${ctx}/static/plugins/easyui/common/bitbug_favicon.ico" type="image/x-icon" />

<script src="${ctx}/static/plugins/easyui/jquery/jquery-1.11.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/login.css" />
</head>
<body>
	<div>
		<form id="loginForm" action="${ctx}/login" method="post">
			<div class="login_content_backbg">
				<div class="login_content">
					<div class="login_serloging">&nbsp;</div>
					<div class="login_content_area">
						<ul>
							<li class="wrong_prompt">&nbsp;</li>
							<li><input class="login_input_name" type="text" id="username" name="username" placeholder="请输入用户名" /></li>
							<li><input class="login_input_password" type="password" id="password" name="password" placeholder="请输入密码" /></li>
							<li>
								<button class="login_button"><font color="#ffffff">登&nbsp;&nbsp;&nbsp;录</font></button>
							</li>
						</ul>
					</div>
					<!--  <div class="login_information">版权所有:某某某软件公司     2000-2011 某某某管理平台 京ICP备05051632号</div>  -->
				</div>
			</div>
		</form>
	</div>
	<c:choose>
		<c:when test="${error eq 'org.apache.shiro.authc.UnknownAccountException'}">
			<script>
				$(".wrong_prompt").html("用户名不存在，请重试！");
			</script>
		</c:when>
		<c:when test="${error eq 'org.apache.shiro.authc.IncorrectCredentialsException'}">
			<script>
				$(".wrong_prompt").html("帐号或密码错误，请重试！");
			</script>
		</c:when>
	</c:choose>
</body>
</html>
