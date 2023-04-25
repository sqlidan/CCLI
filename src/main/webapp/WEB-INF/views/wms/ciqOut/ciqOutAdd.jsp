<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/wms/ciqoutinfo/${action}" method="post">
		<div id="tb" style="padding:5px;height:auto">
	  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openCodeList()">基础数据导入</a>
		</div>
		<table class="formTable">
			<tr>
				<td>HS编码：</td>
				<td>
				<input type="hidden" id="ida" name="id" value="${bisCiqInfo.id}">
				<input type="hidden" id="idc" name="ciqId" value="${bisCiqInfo.ciqId}">
				<input id="ciqNuma" name="ciqNum"  class="easyui-validatebox" data-options="width: 150, required:'required'"  value="${bisCiqInfo.ciqNum}"  readonly  style="background:#eee">
				</td>
			</tr>
			
			<tr>
				<td>商品名称：</td>
				<td>
				<input id="cargoName" name="cargoName" class="easyui-validatebox" data-options="width: 150" value="${bisCiqInfo.cargoName}" maxlength="10"  readonly  style="background:#eee">
				</td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
				<input  id="scalar" name="scalar" class="easyui-validatebox"  data-options="width: 150"  value="${bisCiqInfo.scalar}" onkeyup="ischeckNum(this)" maxlength="10"> 
				</td>
			</tr>
			<tr>
				<td>净重：</td>
				<td>
				<input id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 150"  value="${bisCiqInfo.netWeight}"  onkeyup="ischeckNum(this)" maxlength="10">
				</td>
			</tr>
			<tr>
				<td>包装种类：</td>
				<td>
				<input id="bagType" name="bagType" class="easyui-validatebox"  data-options="width:150"  value="${bisCiqInfo.bagType}" >
				</td>
			</tr>
			<tr>
				<td>货值：</td>
				<td>
				<input  id="price" name="price" class="easyui-validatebox"  data-options="width: 150" value="${bisCiqInfo.price}" maxlength="10"  onkeyup="ischeckNum(this)">
				</td>
			</tr>
			<tr>
				<td>录入员</td>
				<c:choose>
					<c:when test="${action == 'create'}">
						<td><input id="infoMan" name="recordMan" class="easyui-validatebox"  data-options="width: 150" value="${user}" disabled/></td>
					</c:when>
					<c:otherwise>
						<td><input id="infoMan" name="recordMan" class="easyui-validatebox"  data-options="width: 150" value="${bisCiqInfo.recordMan}" disabled/></td>
					</c:otherwise>
			</c:choose>
			</tr>
			<tr>
				<td>录入时间</td>
				<c:choose>
					<c:when test="${action == 'create'}">
						<td><input id="infoTime" name="recordTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 150" value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/></td>
					</c:when>
					<c:otherwise>
						<td><input id="infoTime" name="recordTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 150" value="<fmt:formatDate value="${bisCiqInfo.recordTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/></td>
					</c:otherwise>
			</c:choose>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
				<input type="text" id="remark1" name="remark1" class="easyui-validatebox"  data-options="width: 150" maxlength="25" value="${bisCiqInfo.remark1}">
				</td>
			</tr>

		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
$(function(){   
	if(action == "copy"){
		$("#ida").val("");
	}
	if(action == "create"){
		$("#idc").val("${ciqId}");
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
				$("#ciqNuma").val(row.code);
				$("#cargoName").val(row.cargoName);
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