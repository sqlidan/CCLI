<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/wms/customsinfo/${action}" method="post">
	<div id="tb" style="padding:5px;height:auto">
	  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openCodeList()">基础数据导入</a>
	</div>
		<table class="formTable">
			<tr>
				<td>项号：</td>
				<td>
					<input type="hidden" id="ida" name="id" value="${bisCustomsInfo.id}">
					<input type="hidden" id="idc" name="cusId" value="${bisCustomsInfo.cusId}">
					<input class="easyui-validatebox"  id="itemNum" name="itemNum"  data-options="width: 150, required:'required'" value="${bisCustomsInfo.itemNum}"  onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="6"   readonly  style="background:#eee">
				</td>
			</tr>
			<tr>
				<td>HS编码：</td>
				<td><input id="cdNuma" name="cdNum"  class="easyui-validatebox" data-options="width: 150, required:'required'"  value="${bisCustomsInfo.cdNum}"   readonly  style="background:#eee"></td>
			</tr>
			
			<tr>
				<td>商品名称：</td>
				<td>
				<input id="cargoName" name="cargoName" class="easyui-validatebox" data-options="width: 150" value="${bisCustomsInfo.cargoName}" maxlength="10"   readonly  style="background:#eee">
				</td>
			</tr>
			<tr>
				<td>规格：</td>
				<td><input id="spec" name="spec" type="text" class="easyui-validatebox"  data-options="width: 150 " value="${bisCustomsInfo.spec}" maxlength="15"   readonly  style="background:#eee"></td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
				<input  id="scalar" name="scalar" class="easyui-validatebox"  data-options="width: 150"  value="${bisCustomsInfo.scalar}" onkeyup="ischeckNum(this)" maxlength="10"> 
				</td>
			</tr>
			<tr>
				<td>净重：</td>
				<td>
				<input id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.netWeight}"  onkeyup="ischeckNum(this)" maxlength="10">
				</td>
			</tr>
			<tr>
				<td>单位：</td>
				<td>
				<input id="units" name="units" class="easyui-validatebox"  data-options="width:150"  value="千克" style="background:#eee"  readonly>
				</td>
			</tr>
			<tr>
				<td>目的地：</td>
				<td>
				<input  id="destination" name="destination" class="easyui-validatebox"  data-options="width: 150"  value="${bisCustomsInfo.destination}"  maxlength="20">
				</td>
			</tr>
			<tr>
				<td>单价：</td>
				<td>
				<input type="text" id="unitPrice" name="unitPrice" class="easyui-validatebox"  data-options="width: 150" value="${bisCustomsInfo.unitPrice}" maxlength="10">
				</td>
			</tr>
			<tr>
				<td>总价：</td>
				<td>
				<input type="text" id="totalPrices" name="totalPrices" class="easyui-validatebox"  data-options="width: 150" value="${bisCustomsInfo.totalPrices}" maxlength="10">
				</td>
			</tr>
			<tr>
				<td>币种：</td>
				<td>
				<select  id="currencyType" name="currencyType" class="easyui-combobox"  data-options="width: 150"  >
				</select>
				</td>
			</tr>
			<tr>
				<td>免征：</td>
				<td>
				<input type="checkbox" id="freeLavyC"   data-options="width: 150" >
				<input type="hidden" id="freeLavy" name="freeLavy" value="0">
				</td>
			</tr>
			<tr>
				<td>关税：</td>
				<td>
				<input type="text" id="duty" name="duty" class="easyui-validatebox"  data-options="width: 150"  value="${bisCustomsInfo.duty}" maxlength="10">
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
				<input type="text" id="remark1" name="remark1" class="easyui-validatebox"  data-options="width: 150" maxlength="25" value="${bisCustomsInfo.remark1}">
				</td>
			</tr>
			<tr>
				<td>录入员</td>
				<c:choose>
					<c:when test="${action == 'create'}">
						<td><input id="infoMan" name="recordMan" value="${user}" class="easyui-validatebox"  data-options="width: 150" disabled/></td>
					</c:when>
					<c:otherwise>
						<td><input id="infoMan" name="recordMan" value="${bisCustomsInfo.recordMan}" class="easyui-validatebox"  data-options="width: 150" disabled/></td>
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
						<td><input id="infoTime" name="recordTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 150" value="<fmt:formatDate value="${bisCustomsInfo.recordTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/></td>
					</c:otherwise>
			</c:choose>	
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
		$("#idc").val("${cusId}");
	}
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=currencyType",
		dataType : "json",
		success : function(date) {
			for (var i = 0; i < date.rows.length; i++) {
				$('#currencyType').combobox({
					data : date.rows,
					valueField : 'value',
					textField : 'label',
					editable : false,
				});
			}
		}
	});
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
				$("#itemNum").val(row.itemNum);
				$("#cdNuma").val(row.code);
				$("#cargoName").val(row.cargoName);
				$("#spec").val(row.spec);
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