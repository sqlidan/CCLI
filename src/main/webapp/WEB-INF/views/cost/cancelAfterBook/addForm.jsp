<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/cost/cancelafter/addhx" method="post">
		<table class="formTable">
			<tr>
				<td>币种：</td>
				<td>
					<input id="nType" name="nType" type="hidden"  value="${ntype}" />
					<input id="customId" name="customId" type="hidden"  />
					<input id="customName" name="customName" type="hidden" />
					<select class="easyui-combobox" id="currency" name="currency" data-options="width:150,required:'required'">
						<option value=""></option>
					</select>  
				</td>
			</tr>
			<tr> 
				<td>汇率：</td>
				<td>  
					 <input id="nRat" name="nRat" disabled="disabled"  style="width: 145px;" value='0' > 
				</td>
			</tr>
			<tr> 
				<td>已选择金额RMB：</td>
				<td> <input id="ids" name="ids"  type="hidden"  />
				 	 <input id="cType" name="cType"  type="hidden"  value="${ctype}" />
					 <input id="zMoney" name="zMoney"  disabled="disabled"  style="width: 145px;" > 
				</td>
			</tr>
			<tr> 
				<td>核销金额：</td>
				<td>
					<input id="sumMoney" name="sumMoney" class="easyui-numberbox" <c:if test="${ctype == 1}"> readonly="readonly" </c:if>  data-options="width: 150 ,required:'required' ,number:'true',precision:4"  > 
				</td>
			</tr>
			<!--  
			 <tr>
				<td>银行：</td>
				<td>
					<input id="bankName" name="bankName"  type="hidden" class="easyui-validatebox" >
					<select class="easyui-combobox" id="bankNum" name="bankNum" data-options="width:150,required:'required'">
						<option value=""></option>
					</select> 
				</td>
			</tr>
			 -->
		</table>
	</form>
</div>

<script type="text/javascript">
var ctype=${ctype};
$(function(){ 
	//币种  下拉
	$.ajax({
	   	type: "GET",
	   	async : false,
		url: "${ctx}/system/dict/searchDict/"+"currencyType",
	   	data: "",
	   	dataType: "json",
	   	success: function(date){
	   		if(date!=null && date.length>0) {
				$('#currency').combobox({
					data: date,
					valueField: 'value',
					textField: 'label',
					editable: false,
					onSelect:function(){
				   		getid=	$('#currency').combobox("getValue");
				   		$.post("${ctx}/base/taxrate/getobj/"+getid, {},function(data){
				   			    if(data!=null && data.exchangeRate!=null){
				   			    	$("#nRat").val(data.exchangeRate);
				   			    	if(1==ctype){
				   			    		var getmoney=$("#zMoney").val()/data.exchangeRate;
				   			    		$("#sumMoney").val(getmoney);
				   			    		$("input[name='sumMoney']").val(getmoney);
				   			    	}
				   			    }else{
				   			    	$("#nRat").val(0);
				   			    	$("#sumMoney").val(0);
				   			    }
				   		}, "json");
				   	}
				});
			}
		}
	});
	/**
	$.ajax({
	   	type: "GET",
	   	async : false,
		url: "${ctx}/cost/cancelafter/banksjson",
	   	data: "",
	   	dataType: "json",
	   	success: function(date){
	   		if(date!=null && date.length>0) {
				$('#bankNum').combobox({
					data: date,
					valueField: 'BANK_NUM',
					textField: 'BANK_NAME',
					editable: false
				});
			}
		}
	});
	*/
	//获取选中的核销总金额
	$.post("${ctx}/cost/cancelafter/getsummoneyjson", { "ids": $("#postids").val() },function(data){
			    if(data!=null){
			    	$("#zMoney").val(data.SUMMONEY);
			    }
	}, "json");
}); 

 
 
//提交表单
$('#mainform').form({    
    onSubmit: function(){ 
    	$("#ids").val($("#postids").val());
		//$("#bankName").val($('#bankNum').combobox("getText"));
		$("#customId").val($('#receiver').combobox("getValue"));
		$("#customName").val($('#receiver').combobox("getText"));
		var getRat=$("#nRat").val();//汇率  
		if(getRat==null || getRat=="" || getRat==0){
			parent.$.easyui.messager.alert("选择的币种未维护汇率信息！");
			return false;
		}
		var zmoney=$("#zMoney").val();
		var summoney=$("#sumMoney").val();
		if(Number(zmoney)<Number(summoney*getRat)){
			parent.$.easyui.messager.alert("输入的核销金额要小于等于已选择的核销总金额");
			return false;
		}
		
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){   
    	 if("success"==data){
    		 var obj = $("#searchFrom").serializeObject();
 			 dg.datagrid('load', obj);
 			 d.panel('close');
    	 }else if("taxerror"==data){
    		 parent.$.easyui.messager.alert("核销选择的货币类型未维护汇率表！"); 
    	 }else{
    		 parent.$.easyui.messager.alert("核销操作失败");
    	 }
    }    
});    
</script>
</body>
</html>