<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform3" action="${ctx}/wms/preEntryInfo/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>项号：</td>
				<td>
					<input  type="hidden" id="idA" name="id" value="${bisPreEntryInfo.id}">
					<input  type="hidden" id="forIdA" name="forId" value="${bisPreEntryInfo.forId}">
					<input id="itemNum" name="itemNum" class="easyui-validatebox" data-options="width:150, required:'required'" maxlength="10" value="${bisPreEntryInfo.itemNum}" >
				</td>
			</tr>
			<tr>
				<td>编码：</td>
				<td><input id="hscode" name="hscode" type="text" class="easyui-validatebox" data-options="width: 150" maxlength="20" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"  value="${bisPreEntryInfo.hscode}" ></td>
			</tr>
			<tr>
				<td>品名：</td>
				<td>
				<input type="text" id="cargoName" name="cargoName" class="easyui-validatebox" data-options="width: 150 "  value="${bisPreEntryInfo.cargoName}" maxlength="20" >
				</td>
			</tr>
			<tr>
				<td>规格：</td>
				<td><input id="space" name="space" type="text" class="easyui-validatebox"  data-options="width: 150 " value="${bisPreEntryInfo.space}" maxlength="20" ></td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
				<input type="text" id="price" name="price" class="easyui-validatebox"  data-options="width: 150" value="${bisPreEntryInfo.price}" maxlength="10" oninput="ischeckNum(this)" onpropertychange="ischeckNum(this)">
				</td>
			</tr>
			<tr>
				<td>净重：</td>
				<td>
				<input id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 150"  value="${bisPreEntryInfo.netWeight}" maxlength="10" onkeyup="ischeckNum(this)">
				KG</td>
			</tr>
			<tr>
				<td>ETA：</td>
				<td>
				<input id="eta" name="eta" class="easyui-my97"  data-options="width:150" datefmt="yyyy-MM-dd" 
						value="<fmt:formatDate value="${bisPreEntryInfo.eta}" pattern="yyyy-MM-dd" />" >
				</td>
			</tr>
			<tr>
				<td>木托数量：</td>
				<td>
				<input type="text" id="trayNum" name="trayNum" class="easyui-validatebox"  data-options="width: 150" value="${bisPreEntryInfo.trayNum}" maxlength="8" onkeyup="ischeckNum(this)">
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
				<input type="text" id="remark" name="remark" class="easyui-validatebox"  data-options="width: 150" value="${bisPreEntryInfo.remark}" maxlength="25">
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
var action="${action}";
$(function(){   
	if(action == "create"){
		var forId = "${forId}";
		$("#forIdA").val(forId);
	}
	if(action == "copy"){
		$("#idA").val("");
	}
});	
 
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
$('#mainform3').form({
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