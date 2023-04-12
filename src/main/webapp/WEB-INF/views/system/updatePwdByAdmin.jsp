<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.form.js"></script>
</head>
<body>
	<div style="padding: 5px">
	<form id="pwdForm" action="${ctx }/system/user/updatePwdByAdmin" method="post">
	<table>
		<tr>
			<td>原密码：</td>
			<td>
			<input type="hidden" name="id" value="${user.id}"/>
			<input id="oldPassword" name="oldPassword" type="password" class="required"/>
			</td>
		</tr>
		<tr>
			<td>密码：</td>
			<td><input id="plainPasswordi" name="plainPassword" type="password" class="required" minlength="6" maxlength="20"/></td>
		</tr>
		<tr>
			<td>确认密码：</td>
			<td><input id="confirmPasswordi" name="confirmPassword" type="password" class="required" equalTo="#plainPassword"/></td>
		</tr>
	</table>
	</form>
</div>
<!--<input type="button" onclick="goSure()">确定</input> -->
<script>
$(function(){
	$('#oldPassword').validatebox({    
	   	 	required: true, 
	    	validType:{
	    	remote:["${ctx}/system/user/checkPwd/"+${user.id},"oldPassword"]
	    }
	}); 
});

$('#pwdForm').form({
	onSubmit : function() {
		var isValid = $(this).form('validate');
		return isValid; // 返回false终止表单提交
	},
	success : function(data) {
		parent.$.messager.show({title: "提示", msg: "修改成功！", position: "bottomRight" });
	} 
});
</script>
</body>
</html>