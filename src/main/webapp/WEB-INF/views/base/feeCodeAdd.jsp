<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx}/base/feecode/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>费目代码：</td>
				<td>
					<input type="hidden" id="id" name="id" value="${id}"/>
					<input id="code" name="code" class="easyui-validatebox" data-options="width: 150, required:'required' " value="${feeCode.code }" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /> 
				</td>
			</tr>
			<tr>
				<td>费目名称：</td>
				<td><input id="nameC" name="nameC" class="easyui-validatebox" data-options="width: 150, required:'required' "  value="${feeCode.nameC }" maxlength="10"/></td>
			</tr>
			<tr>
				<td>费目英文名称：</td>
				<td><input id="nameE" name="nameE" type="text" class="easyui-validatebox" data-options="width: 150 ,prompt: '请输入英文'" value="${feeCode.nameE }"  maxlength="40"/></td>
			</tr>
			
			<tr>
				<td>费目类别：</td>
				<td>
				<input type="hidden" id="selectfee" value="${feeCode.feeType }" />
				<select type="select" id="feeType" name="feeType" class="easyui-combobox" data-options="width: 150 " >
				</select>
				</td>
			</tr>
			<tr>
				<td>发票名称：</td>
				<td><input id="nameInvoice" name="nameInvoice" type="text" class="easyui-validatebox"  data-options="width: 150 " value="${feeCode.nameInvoice}" /></td>
			</tr>
<!--		<tr>
				<td>基础价格：</td>
				<td>
				<input type="text" id="priceBase" name="priceBase" class="easyui-validatebox"  data-options="width: 150, required:'required' " onkeyup="ischeckNum()" value="${feeCode.priceBase}"/>
				</td>
			</tr>
-->
			<tr>
				<td>计量单位：</td>
				<td>
				<input type="hidden" id="selectunits" value="${feeCode.units }"/>
				<select id="units" name="units" class="easyui-combobox" data-options="width: 150  " >
				</select>
				</td>
			</tr> 
			<tr>
				<td>条件属性：</td>
				<td>
				<input type="hidden" id="selectterm" value="${feeCode.termAttribute}"/>
				<select id="termAttribute" name="termAttribute" class="easyui-combobox" data-options="width: 150  "  >
				</select>
				</td>
			</tr>
			<tr>
				<td>分拣上限：</td>
				<td>
				<input type="text" id="maxPrice" name="maxPrice" class="easyui-validatebox"  data-options="width: 150" onkeyup="ischeckNum()" value="${feeCode.maxPrice}"/>
				</td>
			</tr>
			<tr>
				<td>分拣下限：</td>
				<td>
				<input type="text" id="minPrice" name="minPrice" class="easyui-validatebox"  data-options="width: 150" onkeyup="ischeckNum()" value="${feeCode.minPrice}"/>
				</td>
			</tr>
			<tr>
				<td>币种：</td>
				<td>
				<input type="hidden" id="selectcurr" value="${feeCode.currencyType }" />
				<select id="currencyType" name="currencyType" class="easyui-combobox"  data-options="width:150 , required:'required'" >
				</select>
				</td>
			</tr>
			<tr>
				<td>买方承担：</td>
				<td>
				<input type="hidden" id="selectbuy" value="${feeCode.buyBill}" />
				<select id="buyBill" name="buyBill"class="easyui-combobox" data-options="width: 150"  > 
					<option value="0">不承担</option>
					<option value="1">承担</option>
				</select>
				</td>
			</tr>
			<tr>
				<td>卖方承担：</td>
				<td>
				<input type="hidden" id="selectsell" value="${feeCode.sellBill}" />
				<select id="sellBill" name="sellBill" class="easyui-combobox" data-options="width: 150" > 
					<option value="0">不承担</option>
					<option value="1">承担</option>
				</select>
				</td>
			</tr>
            <tr>
				<td>付款单费目：</td>
				<td>
					<input type="radio" id="iffukuandan1" name="fukuandan" value="0"/><label for="woman">否</label>
					<input type="radio" id="iffukuandan2" name="fukuandan" value="1"/><label for="man">是</label>
				</td>
			</tr>
			<tr>
				<td>集团费目编码：</td>
				<td><input id="ysCode" name="ysCode" class="easyui-validatebox" data-options="width: 150"  value="${feeCode.ysCode }" maxlength="10"/></td>
			</tr>
			<tr>
				<td>集团费目名称：</td>
				<td><input id="ysInfo" name="ysInfo" class="easyui-validatebox" data-options="width: 150 "  value="${feeCode.ysInfo }" maxlength="10"/></td>
			</tr>
			<tr>
				<td>应付编码：</td>
				<td><input id="yfCode" name="yfCode" class="easyui-validatebox" data-options="width: 150 "  value="${feeCode.yfCode }" maxlength="10"/></td>
			</tr>
			<tr>
				<td>应付摘要：</td>
				<td><input id="yfInfo" name="yfInfo" class="easyui-validatebox" data-options="width: 150"  value="${feeCode.yfInfo }" maxlength="10"/></td>
			</tr>
			<tr>
				<td>科目编码：</td>
				<td><input id="jdNum" name="jdNum" class="easyui-validatebox" data-options="width: 150"  value="${feeCode.jdNum }" maxlength="10"/></td>
			</tr>
			
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
//用户 添加修改区分
if(action=='create'){
	//用户名存在验证
	$('#code').validatebox({    
	    required: true, 
	    prompt: '请输入数字或英文简写',   
	    validType:{
	    	length:[1,6],
	    	remote:["${ctx}/base/feecode/checkFeeCode","code"]
	    }
	});  
	selectXL();
}else if(action=='update'){
	$("input[name='code']").attr('readonly','readonly');
	$("input[name='code']").css('background','#eee')
//	$("input[name='gender'][value=${user.gender}]").attr("checked",true);
    selectXL();
    changeSel();   

}else if(action=='copy'){
   //清空id,code
   $("#id").val("");
   $("#code").val("");
   //用户名存在验证
	$('#code').validatebox({    
	    required: true,    
	    validType:{
	    	length:[1,6],
	    	remote:["${ctx}/base/feecode/checkFeeCode","code"]
	    }
	});  
	selectXL();
    changeSel();   
}
 
 //数字校验
function ischeckNum() {
	var num = document.getElementById('priceBase').value;
	if (num) {
		if (!isNaN(num)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#priceBase").val("");
			myfm.isnum.select();
			return false;
		}
	}
}
//下拉列表ajax
function selectXL() {
	var selfee = $("#selectfee").val();
	var seluni = $("#selectunits").val();
	var selcurr = $("#selectcurr").val();
 	var tjsx = $("#selectterm").val(); 
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/getjson",
		data : "filter_LIKES_type=units",
		dataType : "json",
		success : function(date) {
				$('#units').combobox({
					data : date.rows,
					value : seluni,
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/getjson",
		data : "filter_LIKES_type=feeType",
		dataType : "json",
		success : function(date) {
				$('#feeType').combobox({
					data : date.rows,
					value : selfee,
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/getjson",
		data : "filter_LIKES_type=currencyType",
		dataType : "json",
		success : function(date) {
				$('#currencyType').combobox({
					data : date.rows,
					value : selcurr,
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/getjson",
		data : "filter_LIKES_type=termAttribute",
		dataType : "json",
		success : function(date) {
				$('#termAttribute').combobox({
					data : date.rows,
					value : tjsx,
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});
}

//修改页面，显示下拉框数据
function changeSel() {
	var selbuy = $("#selectbuy").val();
	var selsell = $("#selectsell").val();
	
	if(selbuy == '1'){
		$("#buyBill  option[value='1'] ").attr("selected",true);
	}
	if(selsell =='1'){
		$("#sellBill  option[value='1'] ").attr("selected",true);
	}
}

//客户类型 添加
if(action=='create'){
	$("input[name='fukuandan'][value=0]").attr("checked",true); 
}else if(action=='update'){
	var fukuandan="${feeCode.fukuandan}";
	if(fukuandan!=null&&!""==fukuandan){
	   $("input[name='fukuandan'][value=${feeCode.fukuandan }]").attr("checked",true);
	}
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