<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/cost/inspecioninfo/${actions}" method="post">
		<table class="formTable">
			<tr>
				<td>箱号：</td>
				<td>
					<input type="hidden" id="id" name="id" value="${info.id}"/>
					<input type="hidden" id="feeIda" name="feeId" value="${feeId}"/>
					<input type="hidden" id="billNuma" name="billNum" value="${billNum}"/>
					<input type="hidden" id="standingNum" name="standingNum" value="${info.standingNum}"/>
					<input id="ctnNuma" name="ctnNum" class="easyui-validatebox" data-options="width: 150, required:'required'" value="${info.ctnNum}" /> 
				</td>
				<td>查验标准：</td>
				<td>
				  <input type="text" id="checkStandard" name="checkStandard" class="easyui-validatebox" data-options="width: 150 "
                           value="${info.checkStandard}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
				<!-- <select id="checkStandard" name="checkStandard" class="easyui-combobox" data-options="width: 150, required:'required'" > 
					<option  value="300">300</option>
					<option  value="800">800</option>
					<option  value="500">500</option>
					<option  value="150">150</option>
					<option  value="225">225</option>
					<option  value="200">200</option>
					<option  value="0">0</option>
				</select> -->
				</td>
			</tr>
			<tr>
				<td>是否插电：</td>
				<td>
					<input id="ifPlug" name="ifPlug"   type="checkbox"  value="1" <c:choose><c:when test="${info.ifPlug == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose> /> 
				</td>
				<td>插电标准：</td>
				<td>
				<input type="text" id="plugUnit" name="plugUnit" class="easyui-validatebox" data-options="width: 150 "
                           value="${info.plugUnit}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
				<!-- <select id="plugUnit" name="plugUnit" class="easyui-combobox" data-options="width: 150"> 
					<option value="180" select="select">180</option>
				</select> -->
				</td>
			</tr>
			<tr>
				<td>插电天数：</td>
				<td>
					<input id="plugDays" name="plugDays" class="easyui-validatebox" data-options="width: 150" value="${info.plugDays}" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" oninput="getPlug()" onpropertychange="getPlug()" /> 
				</td>
				<td>插电费用：</td>
				<td>
					<input id="plugPrice" name="plugPrice" class="easyui-validatebox" data-options="width: 150" value="${info.plugPrice}" readonly /> 
				</td>
			</tr>
			<tr>
				<td>是否吊箱：</td>
				<td>
					<input id="ifHang" name="ifHang"   type="checkbox"  value="1" <c:if test="${info.ifHang ==1}">checked</c:if>/> 
				</td>
				<td>吊箱标准：</td>
				<td>
				<input type="text" id="hangUnit" name="hangUnit" class="easyui-validatebox" data-options="width: 150 "
                           value="${info.hangUnit}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
				<!-- <select id="hangUnit" name="hangUnit" class="easyui-combobox" data-options="width: 150" > 
					<option  value="100">100</option>
					<option  value="150">150</option>
					<option  value="200">200</option>
				</select> -->
				</td>
			</tr>
			<tr>
				<td>吊箱次数：</td>
				<td>
					<input id="hangTimes" name="hangTimes" class="easyui-validatebox" data-options="width: 150" value="${info.hangTimes}" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"  oninput="getHang()" onpropertychange="getHang()" /> 
				</td>
				<td>吊箱费用：</td>
				<td>
					<input id="hangPrice" name="hangPrice" class="easyui-validatebox" data-options="width: 150" value="${info.hangPrice}" readonly /> 
				</td>
			</tr>
			<tr>
				<td>是否场地：</td>
				<td>
					<input id="ifField" name="ifField"   type="checkbox"  value="1" <c:if test="${info.ifField ==1}">checked</c:if>/> 
				</td>
				<td>场地标准：</td>
				<td>
				<input type="text" id="fieldUnit" name="fieldUnit" class="easyui-validatebox" data-options="width: 150 "
                   value="${info.fieldUnit}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
				<!-- <select id="fieldUnit" name="fieldUnit" class="easyui-combobox" data-options="width: 150" > 
					<option   value="50">50</option>
					<option   value="100">100</option>
					<option   value="200">200</option>
				</select> -->
				</td>
			</tr>
			<tr>
				<td>场地天数：</td>
				<td>
					<input id="fieldDays" name="fieldDays" class="easyui-validatebox" data-options="width: 150" value="${info.fieldDays}" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"  oninput="getField()" onpropertychange="getField()" /> 
				</td>
				<td>场地费用：</td>
				<td>
					<input id="fieldPrice" name="fieldPrice" class="easyui-validatebox" data-options="width: 150" value="${info.fieldPrice}" readonly /> 
				</td>
			</tr>
			<tr>
				<td>是否搬倒：</td>
				<td>
					<input id="ifHanding" name="ifHanding"   type="checkbox"  value="1" <c:if test="${info.ifHanding ==1}">checked</c:if>/> 
				</td>
				<td>搬倒标准：</td>
				<td>
				  <input type="text" id="handingUnit" name="handingUnit" class="easyui-validatebox" data-options="width: 150 "
                   value="${info.handingUnit}" onkeyup="value=value.replace(/[^\d]/g,'')"  oninput="getHand()" onpropertychange="getHand()"/>
				<!-- <select id="handingUnit" name="handingUnit"  class="easyui-combobox" data-options="width: 150" > 
					<option id="handfy" value="0">0</option>
					<option id="handfy" value="300">300</option>
					<option id="handfy" value="500">500</option>
				</select> -->
				</td>
			</tr>
			<tr>
				<td>搬倒费用：</td>
				<td>
					<input id="handingPrice" name="handingPrice" class="easyui-validatebox" data-options="width: 150" value="${info.handingPrice}" readonly /> 
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
				<input id="remarka" name="remark" class="easyui-validatebox" data-options="width: 150" value="${info.remark}" /> 
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var actions="${actions}";
$(function(){ 
//下拉框改变时计算费用
	/* $("#hangUnit").combobox({
		onSelect:function(){getHang()}
	})
	$("#fieldUnit").combobox({
		onSelect:function(){getField()}
	})
	$("#handingUnit").combobox({
		onSelect:function(){getHand()}
	}) */

if(actions == "create"){
	
}else if(actions == "update"){
	/* var check="${info.checkStandard}";
	var hangfy="${info.hangUnit}";
	var fieldfy="${info.fieldUnit}";
	var handfy="${info.handingUnit}"; */
	
	/* $("#hangUnit").combobox("setValue",hangfy);
	$("#fieldUnit").combobox("setValue",fieldfy);
	$("#handingUnit").combobox("setValue",handfy);
	
	if(parseInt(check)==800){
  		$("#checkStandard  option[value='800'] ").attr("selected",true);
  	}else if(parseInt(check)==500){
  		$("#checkStandard  option[value='500'] ").attr("selected",true);
  	}else if(parseInt(check)==300){
  		$("#checkStandard  option[value='300'] ").attr("selected",true);
  	}else if(parseInt(check)==150){
  		$("#checkStandard  option[value='150'] ").attr("selected",true);
  	}else if(parseInt(check)==0){
  		$("#checkStandard  option[value='0'] ").attr("selected",true);
  	}else if(parseInt(check)==200){
  		$("#checkStandard  option[value='200'] ").attr("selected",true);
  	}else if(parseInt(check)==225){
  		$("#checkStandard  option[value='225'] ").attr("selected",true);
  	} */
  }
})
//保存前校验
function savejy(){
	if( $("#ifPlug").is(":checked") ){
		if( $("#plugDays").val()==""){
			return "false1";
		}
	}
	if( $("#ifHang").is(":checked") ){
		if( $("#hangUnit").val()=="" || $("#hangTimes").val()==""){
			return "false2";
		}
	}
	if( $("#ifField").is(":checked") ){
		if( $("#fieldUnit").val()=="" || $("#fieldDays").val()==""){
			return "false3";
		}
	}
	if( $("#ifHanding").is(":checked") ){
		if( $("#handingUnit").val()==""){
			return "false4";
		}
	}
	return "true";
}

//计算插电费用
function getPlug(){
	$("#plugPrice").val($("#plugUnit").val()*$("#plugDays").val() );
}

//计算吊箱费用
function getHang(){
	if( $("#hangTimes").val()!=""){
		$("#hangPrice").val($("#hangUnit").val()*$("#hangTimes").val());
	}
}

//计算场地费用
function getField(){
	if( $("#fieldDays").val()!=""){
		$("#fieldPrice").val($("#fieldUnit").val()*$("#fieldDays").val());
	}
}

//计算搬倒费用
function getHand(){
	$("#handingPrice").val($("#handingUnit").val());
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
	success : function(msg) {
		location.reload();
	}
});
</script>
</body>
</html>