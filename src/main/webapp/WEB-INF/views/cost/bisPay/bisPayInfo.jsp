<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/cost/bispayinfo/${action}" method="post">
		<table class="formTable" >
			<tr>
				<td>
					<input type="hidden" name="shareState" value="${bisPayInfo.shareState}"/>
					<input type="hidden" name="checkSign" value="${bisPayInfo.checkSign}"/>
					<input type="hidden" name="standingNum" value="${bisPayInfo.standingNum}"/>
					<input type="hidden" id="id" name="id" value="${bisPayInfo.id}"/>
					<input type="hidden" id="stockId" name="stockId" value="${bisPay.clientId}"/>
					
					<c:choose>
						<c:when test="${action == 'create'}">
							<input type="hidden" id="payIda" name="payId"  value="${payId}"/>
						</c:when>
						<c:otherwise> 
							<input type="hidden" id="payIda" name="payId" value="${bisPayInfo.payId}"/>
						</c:otherwise>
				</c:choose>
				</td>
			</tr>
			<!-- 
			<tr>
				<td>销售人员：</td>
				<td>
					<input id="sellMan" name="sellMan" class="easyui-validatebox" data-options="width: 180" value="${bisPayInfo.sellMan}"   > 
				</td>
			</tr>
			 -->
			<tr>
				<td>客户：</td>
				<td>
				<input  id="clientNamea" name="clientName" type="text" class="easyui-validatebox" data-options="width: 180,required:'required'"   value="${bisPayInfo.clientName}"  readonly style="background:#eee"/>
				<input type="hidden" id="clientIda" name="clientId"  value="${bisPayInfo.clientId}"  />
				</td>
			</tr>
			<tr>
				<td>提单号：</td>
				<td>
				<input type="text" id="billNum" name="billNum" class="easyui-searchbox" data-options="prompt:'请选择提单号',required:'required',width:180,searcher:doSearch" value="${bisPayInfo.billNum}" readonly/>
				</td>
			</tr>
			<tr>
				<td>费目：</td>
				<td>
					<input type="hidden" id="feeName" name="feeName" value="${bisPayInfo.feeName}"/>
					<select id="feeCode" name="feeCode"  class="easyui-combobox" data-options="width: 180, required:'required' ">
					</select>
				</td>
			</tr>
			<tr>
				<td>单价：</td>
				<td>
				<input type="text" id="unitPrice" name="unitPrice" class="easyui-validatebox" data-options="width: 180, required:'required' " value="${bisPayInfo.unitPrice}" onkeyup="ischeckNum(this)"   oninput="getNum2(this)" onpropertychange="getNum2(this)" />
				</td>
			</tr>
			<tr>
				<td>数量：</td>
				<td>
				<input type="text" id="num" name="num" class="easyui-validatebox" data-options="width: 180 " value="${bisPayInfo.num}" readonly/>
				</td>
			</tr>
			<tr>
				<td>金额：</td>
				<td>
				<input type="text" id="totelPrice" name="totelPrice" class="easyui-validatebox" data-options="width: 180, required:'required'" value="${bisPayInfo.totelPrice}" onkeyup="ischeckNumForCZ(this)" oninput="getNum(this)" onpropertychange="getNum(this)"/>
				</td>
			</tr>
			<tr>
				<td>税率：</td>
				<td>
				<select  id="taxRate" name="taxRate" class="easyui-combobox" data-options="width: 180, required:'required'" value="${bisPayInfo.taxRate}" >
				</select>
				</td>
			</tr>
			 <!-- 
			<tr>
				<td>箱量：</td>
				<td>
				<input id="ctnAmount" name="ctnAmount" type="text" class="easyui-validatebox"  data-options="width: 180 " value="${bisPayInfo.ctnAmount}"  onkeyup="ischeckNum(this)" >
				</td>
			</tr>
			 -->
			<tr>
				<td>是否垫付：</td>
				<td>
				<select   id="helpPay"   name="helpPay" class="easyui-combobox"  data-options="width: 180"  >
					<option value='1'>垫付</option>
					<option value='0'>代理</option>
				</select>
				</td>
			</tr>
						<tr>
				<td>是否冲账：</td>
				<td>
				<select   id="ifReverse"   name="ifReverse" class="easyui-combobox"  data-options="width: 180"  >
					<option value='0'>否</option>
					<option value='1'>是</option>
				</select>
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
				<input type="text" id="remark"   name="remark" class="easyui-validatebox"  data-options="width: 180" value="${bisPayInfo.remark}" />
				</td>
			</tr>
		</table>
	</form>
</div>
<div id="dlg2" ></div> 
<script type="text/javascript">
var actionInfo="${action}";
var payId="${payId}";
var ddlg;
//c为保存时或联动时判断客户是否为下拉框选择的最终标示符，d为判断上次change是否为下拉框选择的标示符
var c=0;
var d;
$(document).ready(function(){
	if(actionInfo=='copy'){
   //清空id 
   	$("#id").val("");
	}
	selectXL();
	if(actionInfo=='update'){
		var pays="${bisPayInfo.helpPay}";
		var ifReverse ="${bisPayInfo.ifReverse}";
		if(pays == '0'){
			$("#helpPay  option[value='0'] ").attr("selected",true);
		}
		if(ifReverse == '0'){
			$("#ifReverse  option[value='0'] ").attr("selected",true);
		}
	}
	window.setTimeout(function(){readonly()},200);
})

function readonly(){
	$("input[name='billNum']").attr("readonly","readonly");
	$("#billNum").attr("readonly","readonly");
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

 //金额的数字检验 根据是或否冲账
 function ischeckNumForCZ(val){
	 var ifr = $("#ifReverse").combobox("getValue");
	 if(ifr == 0){
		 ischeckNum(val);
	 }else{
		 
	 }
	 
 }
 
 
//下拉列表ajax
function selectXL() {
    $("input[name='billNum']").attr("readonly","readonly");
   	//基础费目
	var getFee='${bisPayInfo.feeCode}';
	$('#feeCode').combobox({
	   method:"GET",
	   url:"${ctx}/base/feecode/getFeeAll?setid=${bisPayInfo.feeCode}",
	   valueField: 'code',
	   textField: 'nameC',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getFee!=null && getFee!=""){
				 $('#feeCode').combobox("select",getFee);
				 getFee = "";
			}
	   }
   	});
   	
//税率
	var tax='${bisPayInfo.taxRate}'
	if(actionInfo == "create"){
		tax = '0.06';
	}
   	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=tax",
		dataType : "json",
		success : function(date) {
				$('#taxRate').combobox({
					data : date.rows,
					value : tax,
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});   	
}

//提单号查询
function doSearch(){
	ddlg=$("#dlg2").dialog({   
	    title: '联系单查询',    
	    width: 650,    
	    height:500,    
	    href:'${ctx}/cost/bispay/enterlist',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var row = dglx.datagrid('getSelected');
				$("#billNum").searchbox('setValue', row["ITEMNUM"]);
				$("#clientIda").val(row["STOCK_ORG_ID"]);
				$("#clientNamea").val(row["STOCK_ORG"]);
				selectXL(row["STOCK_ID"]); 
				ddlg.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				ddlg.panel('close');
			}
		}]
	}); 
}

 

//计算数量
function getNum(val){
	if($("#unitPrice").val() != ""){
		var totalprice =  Math.abs(val.value *1);
 		var unitprice =  $("#unitPrice").val()*1 ;
		$("#num").val(totalprice/unitprice);
	} 
}

function getNum2(val){
	if($("#totelPrice").val() != ""){
		var totalprice =  Math.abs($("#totelPrice").val() *1);
 		var unitprice =  val.value *1;;
		$("#num").val(totalprice/unitprice);
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