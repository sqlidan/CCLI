<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx}/base/taxrate/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>币种：</td>
				<td>
				<input type="hidden" name="id" value="${id }"/>
				<input type="hidden" id="selectcurr" value="${BaseTaxRate.currencyType }" >
				<select id="currencyType" name="currencyType"  class="easyui-validatebox" validType="" data-options="width:150 , required:'required'" >
				
				</select>
				</td>
			</tr>
			<tr>
				<td>汇率：</td>
				<td>
				<input type="text" id="exchangeRate" name="exchangeRate"  class="easyui-validatebox"  data-options="width: 150, required:'required' " onkeyup="ischeckNum()" value="${BaseTaxRate.exchangeRate }"/>
				</td>
			</tr>
			<tr>
			    <td>日期</td>
				<td><input name="theDate" class="easyui-my97"  datefmt="yyyy-MM-dd" data-options="width: 150" value="<fmt:formatDate value="${BaseTaxRate.theDate}"/>" pattern="yyyy-MM-dd"/></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
var action="${action}";
//用户 添加修改区分
if(action=='create'){
	//币种存在验证
	$('#currencyType').validatebox({ 
	    required: true, 
	    validType:{
	    	remote:["${ctx}/base/taxrate/checkCurrency","currencyType"]
	    }
	});  
selectXL();
}else if(action=='update'){
    selectXL();
}
 

 //数字校验
function ischeckNum() {
	var num = document.getElementById('exchangeRate').value;
	if (num) {
		if (!isNaN(num)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#exchangeRate").val("");
			myfm.isnum.select();
			return false;
		}
	}
}
//下拉列表ajax
function selectXL() {
	var selcurr = $("#selectcurr").val();
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
					value : selcurr,
					valueField : 'value',
					textField : 'label',
					editable : false,
					onChange : function(newVal,oldVal){
					   yz(newVal,oldVal);
					}
				});
			}
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

//验证币种是否重复
function yz(newVal,oldVal){
var postUrl = "${ctx}/base/taxrate/checkCurrency";
 	 $.post(postUrl, {"currency": newVal },function(data){
	     if(data != null){
	        if(data == "false"){
	           $("#currencyType").val("0");
	           selectXL();
	        }
	     }
	}, "text");
}

</script>
</body>
</html>