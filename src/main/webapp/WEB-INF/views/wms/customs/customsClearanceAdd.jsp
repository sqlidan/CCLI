<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/wms/customs/clearance/info/${action}" method="post">
	<!-- <div id="tb" style="padding:5px;height:auto">
	  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openCodeList()">基础数据导入</a>
	</div> -->
		<table class="formTable">
			<tr>
				<td>商品名称：</td>
				<td>
					<input type="hidden" id="ida" name="id" value="${bisCustomsInfo.id}">
					<input type="hidden" id="idc" name="cusId" value="${bisCustomsInfo.cusId}">
					<input class="easyui-validatebox"  id="commodityName" name="commodityName"  data-options="width: 150" value="${bisCustomsInfo.commodityName}">
				</td>
			</tr>
			<tr>
				<td>拉丁文名：</td>
				<td><input id="latinName" name="latinName"  class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.latinName}"  ></td>
			</tr>
			
			<tr>
				<td>商品编码：</td>
				<td><input id="commodityCode" name="commodityCode"  class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.commodityCode}"  ></td>
			</tr>
			
			<tr>
				<td>规格：</td>
				<td><input id="specification" name="specification"  class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.specification}"  ></td>
			</tr>
			
			<tr>
				<td>数量：</td>
				<td><input id="num" name="num"  class="easyui-numberbox" data-options="width: 150, required:'required'" value="${bisCustomsInfo.num}" precision="3"></td>
				<td>
				<label id="sumCounts"/>
				</td>
			</tr>
			
			<tr>
				<td>净重：</td>
				<td><input id="netWeight" name="netWeight"  class="easyui-numberbox" data-options="width: 150,required:'required'" value="${bisCustomsInfo.netWeight}"  precision="3"></td>
			</tr>
			
			<tr>
				<td>毛重：</td>
				<td><input id="grossWeight" name="grossWeight"  class="easyui-numberbox" data-options="width: 150, required:'required'" value="${bisCustomsInfo.grossWeight}"  precision="3"></td>
			</tr>
			
			<tr>
				<td>金额：</td>
				<td><input id="money" name="money"  class="easyui-numberbox" data-options="width: 150, required:'required'" value="${bisCustomsInfo.money}" precision="3"></td>
			</tr>
			
			<tr>
				<td>币制：</td>
				<td>
				<select id="currencyValue" name="currencyValue" class="easyui-combobox" data-options="width: 150">	
					<option value="1">美元</option>	
					<option value="2">日元</option>	
					<option value="3">欧元</option>
					<option value="4">英镑</option>
					<option value="0">人民币</option>							
				</select>
				</td>
			</tr>
			
			<tr>
				<td>生产企业名称及注册号：</td>
				<td><input id="firmName" name="firmName"  class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.firmName}"  ></td>
			</tr>
			
			<tr>
				<td>包装形式：</td>
				<td><input id="packagedForm" name="packagedForm"  class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.packagedForm}"  ></td>
			</tr>
			
			<tr>
				<td>有无木质包装：</td>
				<td><select id="ifWoodenPacking" name="ifWoodenPacking"  class="easyui-combobox" data-options="width: 150 " editable="false" >
					<option  value="0">无</option>	
					<option  value="1">有</option>	
				</select>
				</td>
			</tr>
			<tr>
				<td>木托编号：</td>
				<td><input id="woodenNo" name="woodenNo"  class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.woodenNo}"  ></td>
			</tr>
			<%-- <tr>
				<td>海关品名：</td>
				<td>
				<input id="cargoName" name="cargoName" class="easyui-validatebox" data-options="width: 150" value="${bisCustomsInfo.cargoName}" maxlength="10">
				</td>
			</tr> --%>
		<%-- 	<tr>
				<td>件数：</td>
				<td>
				<input  id="scalar" name="scalar" class="easyui-validatebox"  data-options="width: 150"  value="${bisCustomsInfo.scalar}" onkeyup="ischeckNum(this)" maxlength="10"> 
				</td>
			</tr>
			<tr>
				<td>重量：</td>
				<td>
				<input id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 150"  value="${bisCustomsInfo.netWeight}"  onkeyup="ischeckNum(this)" maxlength="10">
				</td>
			</tr>
			<tr>
				<td>金额：</td>
				<td>
				<input type="text" id="totalPrices" name="totalPrices" class="easyui-validatebox"  data-options="width: 150" value="${bisCustomsInfo.totalPrices}" maxlength="10">
				</td>
			</tr> --%>
		<%-- 	<tr>
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
			</tr> --%>
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
		$("#idc").val("${idd}");
	}
	if(action == "update"){
		setTimeout(function(){
			// 要延后执行的代码
			getSelected();
			},0);
		
		console.log($('#currencyValue'));
		
	 	
	}
/* 	$.ajax({
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
	}); */
});	

function getSelected(){
	$('#currencyValue').combobox("select",'${bisCustomsInfo.currencyValue}');
	$('#ifWoodenPacking').combobox("select",'${bisCustomsInfo.ifWoodenPacking}');
	
}
//数字校验
function ischeckNum() {
    var num = document.getElementById('priceBase').value;
    if (num) {
        if (!isNaN(num)) {

        } else {
            parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight"});
            $("#priceBaseF").val("");
            myfm.isnum.select();
            return false;
        }
    }
}
$('#ifWoodenPacking').combobox({
	onSelect: function (record) {//下拉框数值改变
		var out = $('#ifWoodenPacking').combobox("getValue");
		if(out ==1 ){
			$('#woodenNo').validatebox({ required: true });
		}else{
			$('#woodenNo').validatebox({ required: false });
		}

	}
})
//数字校验
/* function ischeckNum(val) {
	if (val.value) {
		if (!isNaN(val.value)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#"+val.id).val("");
			myfm.isnum.select();
			return false;
		}
	}
} */

//HS库添加
/* function openCodeList(){
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
}  */


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

//添加修改明细时检查原报入单中的数量	
$('#num').bind('blur',function(){
	var out = $('#serviceProject').combobox("getValue");
	if(out ==1)
	  $.ajax({
			type : "POST",
			url : "${ctx}/wms/customs/clearance/getMaxSum",
			data:{dNumber:$('#customsDeclarationNumber').val()},
			dataType : "text",
			success : function(data) {
			 if(data == "error"){
				 $("#sumCounts").css("color", "red").html("没有找到对应记录");
				 }
			 else{
				 $("#sumCounts").css("color", "red").html("最多可出数量："+data);
				 }
			  
			}
		});
})
</script>
</body>
</html>