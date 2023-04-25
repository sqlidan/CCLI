<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'">
		<form id="mainForm"  action=""  method="post">
			<div style="height:auto" class="datagrid-toolbar">
				<shiro:hasPermission name="wms:extension:set">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="saveIt()">保存</a>
			   	<span class="toolbar-item dialog-tool-separator"></span>
			   	</shiro:hasPermission>
		   	</div>
				<table class="formTable" >
					<tr>
					<td>预警间隔</td>
					<td>
					<input id="id" name="id" type="hidden"  value="${extension.id}" >
					<input id="warningDay" name="warningDay" class="easyui-validatebox"  data-options="width:200,required:'required'"  maxlength='5'  value="${extension.warningDay}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
					</td>
					</tr>
					<tr>
					<td>弹出预警间隔</td>
					<td>
					<input id="outWarningDay" name="outWarningDay" class="easyui-validatebox"  data-options="width:200,required:'required'"   maxlength='5'  value="${extension.outWarningDay}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
					</td>
					<tr>
					<td>延期天数</td>
					<td>
					<input id="extensionDay" name="extensionDay" class="easyui-validatebox"  maxlength='5' data-options="width:200" value="${extension.extensionDay}" onkeyup="value=value.replace(/[^\d]/g,'')" />
					</td>
					</tr>
					<tr>
					<td>延期次数</td>
					<td>
					<input id="extensionNum" name="extensionNum" class="easyui-validatebox"  maxlength='5' data-options="width:200" value="${extension.extensionNum}" onkeyup="value=value.replace(/[^\d]/g,'')" />
					</td>
					</tr>
				</table>
		</form>
</div>

  
<script type="text/javascript">
var dg;
var dgg;
var d;
var asn;
$(function(){   
});

function saveIt(){
	if($("#mainForm").form('validate')){
		if( !isNaN($("#warningDay").val()) && !isNaN($("#outWarningDay").val()) && !isNaN($("#extensionDay").val()) && !isNaN($("#extensionNum").val())){
			//用ajax提交form
	 		$.ajax({
	 	  		async: false,
	 	  		type: 'POST',
	 	  		url: "${ctx}/wms/extension/change",
	 	  		data: $('#mainForm').serialize(),
	 	  		dataType: "text",  
	 	  		success: function(msg){
	 	  			if(msg == "success"){
	 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
	 	  			}else{
	 	  				parent.$.messager.show({ title : "提示",msg: "保存失败！", position: "bottomRight" });
	 	  			}
	 	  		}
	 	  	});
 	  	}else{
 	  		parent.$.messager.show({ title : "提示",msg: "请输入数字！", position: "bottomRight" });
 	  		return;
 	  	}
	}
}

</script>
</body>
</html>