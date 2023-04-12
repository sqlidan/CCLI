<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/wms/gate/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>车牌号：</td>
				<td>
					<input id="gateCarNoNew" name="gateCarNoNew" class="easyui-validatebox"    required="true"  value="${bisGateCar.carNum}"> 
				</td>
				<td>
					<input id="id" name="id" class="easyui-validatebox"  type="hidden"   value="${bisGateCar.id}"> 
				</td>
			</tr>
		
		</table>
	</form>
</div>

<script type="text/javascript">
//提交表单
$('#mainform').form({    
    onSubmit: function(){ 
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){   
    	successTip(data,dg,d);
    }    
});    
</script>
</body>
</html>