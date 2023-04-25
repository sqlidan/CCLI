<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform3" action="${ctx}/wms/outforecastinfo/${action}" method="post">
	<div id="tb" style="padding:5px;height:auto">
	  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openCodeList()">基础数据导入</a>
	</div>
		<table class="formTable">
			<tr>
				<td>提单号：</td>
				<td>
					<input  type="hidden" id="idA" name="id" value="${bisOutForecastInfo.id}">
					<input type="hidden" id="forIdA" name="forId" value="${bisOutForecastInfo.forId}">
					<input id="billNuma" name="billNum" class="easyui-validatebox" data-options="width:150, required:'required'" maxlength="10" value="${bisOutForecastInfo.billNum}">
				</td>
			</tr>
			<tr>
				<td>编码：</td>
				<td><input id="hscode" name="hscode" type="text" class="easyui-validatebox" data-options="width: 150" maxlength="20" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"  value="${bisOutForecastInfo.hscode}"   readonly  style="background:#eee"></td>
			</tr>
			
			<tr>
				<td>品名：</td>
				<td>
				<input type="text" id="cargoName" name="cargoName" class="easyui-validatebox" data-options="width: 150 "  value="${bisOutForecastInfo.cargoName}" maxlength="20"   readonly  style="background:#eee">
				</td>
			</tr>
			<tr>
				<td>规格：</td>
				<td><input id="space" name="space" type="text" class="easyui-validatebox"  data-options="width: 150 " value="${bisOutForecastInfo.space}" maxlength="20"></td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
				<input type="text" id="piece" name="piece" class="easyui-validatebox"  data-options="width: 150" value="${bisOutForecastInfo.piece}" maxlength="10"  oninput="ischeckNum(this)" onpropertychange="ischeckNum(this)">
				</td>
			</tr>
			<tr>
				<td>净重：</td>
				<td>
				<input id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 150"  value="${bisOutForecastInfo.netWeight}" maxlength="10"  oninput="ischeckNum(this)" onpropertychange="ischeckNum(this)">
				KG</td>
			</tr>
			<tr>
				<td>清库/清品名：</td>
				<td>
				<input id="clearStore" name="clearStore" class="easyui-validatebox"  data-options="width:150" value="${bisOutForecastInfo.clearStore}">
				</td>
			</tr>
			<tr>
				<td>木托数量：</td>
				<td>
				<input type="text" id="trayNum" name="trayNum" class="easyui-validatebox"  data-options="width: 150" value="${bisOutForecastInfo.trayNum}" maxlength="8" onkeyup="ischeckNum(this)">
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
				<input type="text" id="remark" name="remark" class="easyui-validatebox"  data-options="width: 150" value="${bisOutForecastInfo.remark}" maxlength="25">
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

//HS库添加
function openCodeList(){
 	dhs=$("#hsdlg").dialog({   
	    title: 'HS编码',    
	    width: 650,    
	    height: 500,    
	    href:'${ctx}/base/hscode/hscodelist',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var row  = $('#hsdg').datagrid('getSelected');
				$("#hscode").val(row.code);
				$("#cargoName").val(row.cargoName);
				$("#space").val(row.spec);
				dhs.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				dhs.panel('close');
			}
		}]
	}); 
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