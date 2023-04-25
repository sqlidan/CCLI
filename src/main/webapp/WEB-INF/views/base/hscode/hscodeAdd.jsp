<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/base/hscode/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>HS编码：</td>
				<td>
					<input id="code" name="code"  class="easyui-validatebox" data-options="width: 150, required:'required'"  value="${hscode.code}" <c:if test="${action =='update'}"> readonly style="background:#eee"</c:if> >
				</td>
			</tr>
			<tr>
				<td>单位代码：</td>
				<td>
					<input type="hidden" id="id" name="id" value="${hscode.id}">
					<input class="easyui-validatebox"  id="itemNum" name="itemNum"  data-options="width: 150, required:'required'" value="${hscode.itemNum}"  onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="6"  >
				</td>
			</tr>
			<tr>
				<td>商品名称：</td>
				<td>
				<input id="cargoName" name="cargoName" class="easyui-validatebox" data-options="width: 150,required:'required'" value="${hscode.cargoName}" maxlength="100" >
				</td>
			</tr>
			<tr>
				<td>单位名称：</td>
				<td><input id="spec" name="spec" type="text" class="easyui-validatebox"  data-options="width: 150 " value="${hscode.spec}" maxlength="50" ></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
$(function(){  
	if( action =="create"){ 
		jiaoyan();
	}
});	

function jiaoyan(){
	$('#code').validatebox({    
	   	 	required: true, 
	    	validType:{
	    	remote:["${ctx}/base/hscode/checkhscode","code"]
	    }
	}); 
}


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