<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainFormFeeCode" action="" method="post">
		<table class="formTable">
			<tr>
				<td>费目名称：</td>
				<td><input id="feeName" name="feeName" class="easyui-validatebox" data-options="width: 150, required:'required'"></td>
			</tr>
			<tr>
				<td>费目类别：</td>
				<td>
					<select type="select" id="feeType" name="feeType" class="easyui-validatebox" data-options="width: 150, required:'required' "  >
					</select>
				</td>
			</tr>
			<tr>
				<td>价格：</td>
				<td>
					<input type="text" id="unit" name="unit" class="easyui-validatebox"  data-options="width: 150, required:'required' " onkeyup="ischeckNum(this)" />
				</td>
			</tr>
			<tr>
				<td>币种：</td>
				<td>
					<select id="currency" name="currency" class="easyui-validatebox"  data-options="width:150 , required:'required'" >
					</select>
				</td>
			</tr>
			<tr>
				<td>下限：</td>
				<td>
					<input type="text" id="minPrice" name="minPrice" class="easyui-validatebox"  data-options="width: 150" onkeyup="ischeckNum(this)" />
				</td>
			</tr>
			<tr>
				<td>上限：</td>
				<td>
					<input type="text" id="maxPrice" name="maxPrice" class="easyui-validatebox"  data-options="width: 150 " onkeyup="ischeckNum(this)" />
				</td>
			</tr>
			<tr>
				<td>条件属性：</td>
				<td>
					<select type="select" id="termAttribute" name="termAttribute" class="easyui-validatebox" data-options="width: 150 ">
					</select>
				</td>
			</tr>
			<tr>
				<td>计费方式：</td>
				<td>
					<select type="select" id="billing" name="billing" class="easyui-validatebox" data-options="width: 150, required:'required' "  >
					</select>
				</td>
			</tr>
			<tr>
				<td>档位代码：</td>
				<td>
					<input id="gearCode" name="gearCode" class="easyui-validatebox" data-options="width: 150" ">
				</td>
			</tr>
			<tr>
				<td>档位说明：</td>
				<td>
					<input id="gearExp" name="gearExp" class="easyui-validatebox" data-options="width: 150" ">
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
$(function(){  
	//币种  下拉
	$.ajax({
	   	type: "GET",
	   	async : false,
		url: "${ctx}/system/dict/searchDict/"+"currencyType",
	   	data: "",
	   	dataType: "json",
	   	success: function(date){
	   		for (var i = 0; i < date.length; i++) {
				$('#currency').combobox({
					data: date,
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		}
	});
	
	//费目类别  下拉
	$.ajax({
	   	type: "GET",
	   	async : false,
		url: "${ctx}/system/dict/searchDict/"+"feeType",
	   	data: "",
	   	dataType: "json",
	   	success: function(date){
	   		for (var i = 0; i < date.length; i++) {
				$('#feeType').combobox({
					data: date,
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		}
	});
	
	//条件属性  下拉
	$.ajax({
	   	type: "GET",
	   	async : false,
		url: "${ctx}/system/dict/searchDict/"+"termAttribute",
	   	data: "",
	   	dataType: "json",
	   	success: function(date){
	   		for (var i = 0; i < date.length; i++) {
				$('#termAttribute').combobox({
					data: date,
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		}
	});
	
	//计费方式  下拉
	$.ajax({
	   	type: "GET",
	   	async : false,
		url: "${ctx}/system/dict/searchDict/"+"units",
	   	data: "",
	   	dataType: "json",
	   	success: function(date){
	   		for (var i = 0; i < date.length; i++) {
				$('#billing').combobox({
					data: date,
					valueField: 'value',
					textField: 'label',
					editable: false
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


</script>
</body>
</html>