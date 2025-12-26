<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/platform/reservationData/outbound/create" method="post" >
		<table class="formTable">
			<tr>
				<td>
					<input id="productName" name="productName" class="easyui-validatebox"  data-options="width: 150" value="${productName}" type="hidden">
				</td>
			</tr>
			<tr>
				<td>有效日期：  </td>
				<td><input name="appointDate" type="text" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width: 150,required:'required'" /></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
 
$(document).ready(function(){
		//提交表单
	$('#mainform').form({
		onSubmit: function(){
			var isValid =  $(this).form('validate');
			return isValid;
		},
		success:function(data){
			if(data=="success"){
				d.panel('close');
				successTip(data, dg);
			}else if(data=="warn"){
				parent.$.messager.show({ title : "提示",msg: "请选择同一辆车进行生成！", position: "bottomRight" });
				return;
			}else if(data=="warn1"){
				parent.$.messager.show({ title : "提示",msg: "当前订单信息已生成预约信息，请删除后再生成！", position: "bottomRight" });
				return;
			}else{
				parent.$.messager.show({ title : "提示",msg: "生成预约出库信息失败！", position: "bottomRight" });
				return;
			}
		}
	});
	
});
 
</script>
</body>
</html>