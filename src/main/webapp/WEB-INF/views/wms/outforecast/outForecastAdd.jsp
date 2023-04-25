<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/wms/outforecast/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>客户名称：</td>
				<td>
					<input type="hidden" id="clientName" name="clientName">
					<select id="clientId" name="clientId" class="easyui-combobox" data-options="width:150" >
					</select>
				</td>
			</tr>
			<tr>
				<td>报关公司：</td>
				<td><input id="declarationUnit" name="declarationUnit" type="text" class="easyui-validatebox" data-options="width: 150"  maxlength="30"></td>
			</tr>
			
			<tr>
				<td>报检公司：</td>
				<td>
				<input type="text" id="ciqDeclarationUnit" name="ciqDeclarationUnit" class="easyui-validatebox" data-options="width: 150 "  maxlength="30" >
				</td>
			</tr>
			<tr>
				<td>提单号：</td>
				<td><input id="billNum" name="billNum" type="text" class="easyui-validatebox"  data-options="width: 150 " maxlength="17"></td>
			</tr>
			<tr>
				<td>箱量：</td>
				<td>
				<input type="text" id="ctnCont" name="ctnCont" class="easyui-validatebox"  data-options="width: 150" maxlength="10" onkeyup="ischeckNum(this)">
				</td>
			</tr>
			<tr>
				<td>贸易方式：</td>
				<td>
				<input id="tradeMode" name="tradeMode" class="easyui-validatebox" data-options="width: 150" maxlength="25" >
				</td>
			</tr>
			<tr>
				<td>客服：</td>
				<td>
				<input id="pieceF" name="piece" class="easyui-validatebox"  data-options="width:50"  value="${user}" style="background:#eee"  readonly>
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
				<input type="text" id="remark" name="remark" class="easyui-validatebox"  data-options="width: 150" maxlength="25" >
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
$(function(){   
	ajaxadd();
	
});	
 
function ajaxadd(){
	//客户
	   $('#clientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'ids', 
	   textField: 'clientName',
	   mode:'remote'
   	});
}

//数字校验
function ischeckNum(val) {
	if (val.value) {
		if (!isNaN(val.value)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#"+val.id).val("");
			myfm.isnum.select();
			return false;
		}
	}
}

//提交表单
$('#mainform2').form({
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