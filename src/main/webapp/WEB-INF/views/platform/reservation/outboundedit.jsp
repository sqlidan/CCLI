<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/platform/reservationData/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>车牌号：</td>
				<td>
					<input id="carNumber" name="carNumber"  class="easyui-validatebox" data-options="width: 150, required:'required'"  value="${reservationData.carNumber}" >
				</td>
			</tr>
			<tr>
				<td>联系电话：</td>
				<td>
					<input type="hidden" id="id" name="id" value="${reservationData.id}">
					<input class="easyui-validatebox"  id="driverMobile" name="driverMobile"  data-options="width: 150, required:'required'" value="${reservationData.driverMobile}" >
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";

//提交表单
$('#mainform').form({
	onSubmit : function() {
		var isValid = $(this).form('validate');
		return isValid; // 返回false终止表单提交
	},
	success : function(data) {
		parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight" });
	}
});


</script>
</body>
</html>