<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
	<div>
		<form id="mainform" action="${ctx}/supervision/opAppr/createMani" method="post">
			<table class="formTable">
				<tr data-options="">
					<input type="hidden" id="id" name="id" value="${id}">
					<input type="hidden" id="DeclType" name="DeclType" value="${type}">
					<td>箱号：</td>
					<td><input id="ContaId" name="ContaId"
							   class="easyui-validatebox" data-options="width:150 , required:'required'"/>
					</td>
					<td>车号：</td>
					<td><input id="VehicleId" name="VehicleId"
							   class="easyui-validatebox" data-options="width: 150, required:'required'" value="" /></td>
				</tr>
				<tr>
					<td>车重：</td>
					<td><input id="VehicleWeight" name="VehicleWeight"
							   class="easyui-validatebox"
							   data-options="width: 150, required:'required'" value="" /></td>
					<td>件数：</td>
					<td><input id="qty" name="qty"
							   class="easyui-validatebox"
							   data-options="width: 150, required:'required'" value="" /></td>
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
				if(data == "success"){
					successTip(data,dg,d);
				}else if(data == "over"){
					parent.$.messager.show({title: "提示", msg: "当前核放单申报件数不能超过申请单申报件数!", position: "bottomRight" });
				}else{
					parent.$.messager.show({title: "提示", msg: "保存失败！", position: "bottomRight" });
				}
		    }
		});
	</script>
</body>
</html>