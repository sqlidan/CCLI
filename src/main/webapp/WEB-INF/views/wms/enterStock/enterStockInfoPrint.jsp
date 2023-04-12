<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/wms/enterStockInfo/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>入库联系单id：</td>
				<td>
					<input type="hidden" id="id" name="id" value="${id}"/>
					<input id="linkIdF" name="linkId" class="easyui-validatebox" data-options="width: 150, required:'required'" value="${enterStockInfo.linkId}" readonly/> 
				</td>
			</tr>
			<tr>
				<td>提单号：</td>
				<td>
					<input id="itemNumF" name="itemNum" class="easyui-validatebox" style="width: 150, required:'required'" value="${enterStockInfo.itemNum}" readonly> 
				</td>
			</tr>
			<tr>
				<td>MR：</td>
				<td><input id="ctnNumF" name="ctnNum" type="text" class="easyui-validatebox" data-options="width: 150" value="${enterStockInfo.ctnNum}" ></td>
			</tr>
			
			<tr>
				<td>项目号：</td>
				<td>
				<input type="text" id="projectNumF" name="projectNum" class="easyui-validatebox" data-options="width: 150 " value="${enterStockInfo.projectNum}" >
				</td>
			</tr>
			<tr>
				<td>品名：</td>
				<td><input id="cargoNameF" name="cargoName" type="text" class="easyui-validatebox"  data-options="width: 150 " value="${enterStockInfo.cargoName}" readonly/></td>
			</tr>
			<tr>
				<td>SKU：</td>
				<td>
				<input type="hidden" id="skuH" name="skuH"  value="${enterStockInfo.sku}"/>
				<select type="text" id="skuF" name="sku" class="easyui-validatebox"  data-options="width: 150" >
				</select>
				</td>
			</tr>
			<tr>
				<td>规格：</td>
				<td>
				<input id="typeSizeF" name="typeSize" class="easyui-validatebox" data-options="width: 150"  value="${enterStockInfo.typeSize}" readonly>
				</td>
			</tr>
			<tr>
				<td>箱数：</td>
				<td>
				<input id="pieceF" name="piece" class="easyui-validatebox"  data-options="width:50 , required:'required'"  value="${enterStockInfo.piece}" readonly>
				</td>
			</tr>
			<tr>
				<td>总净重：</td>
				<td>
				<input type="text" id="netWeightF" name="netWeight" class="easyui-validatebox"  data-options="width: 150" onkeyup="ischeckNum()"  value="${enterStockInfo.netWeight}" readonly/>
				</td>
			</tr>
			<tr>
				<td>总毛重：</td>
				<td>
				<input type="text" id="grossWeightF" name="grossWeight" class="easyui-validatebox"  data-options="width: 150" onkeyup="ischeckNum()" value="${enterStockInfo.grossWeight}" readonly/>
				</td>
			</tr>
			<tr>
				<td>重量单位：</td>
				<td>
				<input id="unitsF" name="units" class="easyui-validatebox" value="${enterStockInfo.grossWeight}" readonly onclick="cSku"> 
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
var linkIdN="${linkId}";
//用户 添加修改区分
if(action=='create'){
    $("#linkIdF").val(linkIdN);
    cItemNum();
	selectXL();
}else if(action=='update'){
    selectXL();

}else if(action=='copy'){
   //清空id,code
   $("#idF").val("");
	   selectXL();
}
 

 
 //数字校验
function ischeckNum() {
	var num = document.getElementById('priceBase').value;
	if (num) {
		if (!isNaN(num)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#priceBaseF").val("");
			myfm.isnum.select();
			return false;
		}
	}
}
//下拉列表ajax
function selectXL() {
	var skuH = $("#skuH").val();
	$.ajax({
		type : "GET",
		url : "${ctx}/wms/enterStockInfo/selSku",
		data : "",
		dataType : "json",
		success : function(date) {
			for (var i = 0; i < date.length; i++) {
				$('#skuF').combobox({
					data : date,
					value:skuH,
					valueField : 'skuId',
					textField : 'producingArea',
					editable : false,
					onChange: function (newVal, oldVal){
					   cSku(newVal);
				   }
				});
			}
		}
	});
}

//根据入库联系单号获得提单号
function cItemNum(){
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/enterStockInfo/changeItemNum" ,
		data : "linkId="+linkIdN,
		dataType : "text",
		success : function(date) {
           $("#itemNumF").val(date);
		}
	});
}

//根据skuId获得sku基本信息
function cSku(newVal){
　　var sku =  newVal;
 	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/enterStockInfo/cSku",
		data : "sku="+sku,
		dataType : "json",
		success : function(date) {
           $("#cargoNameF").val(date[0]);
           $("#typeSizeF").val(date[1]);
           $("#pieceF").val(date[2]);
           $("#netWeightF").val(date[3]);
           $("#grossWeightF").val(date[4]);
           $("#unitsF").val(date[5]);
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
		successTip(data, dg, d);
	}
});
</script>
</body>
</html>