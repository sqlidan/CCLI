<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
	
	<form id="mainform" action="${ctx}/cost/piecework/${action}" method="post">
		<table class="formTable" cellpadding="3" cellspacing="3">
					<input type="hidden" id="id" name="id" value="${personTypeRule.id}"/>
					<input type="hidden" id="aaaid" name="aaaid" value="${personTypeRule.personTypeId}"/>
					<input type="hidden" id="aaaName" name="aaaName" value="${personTypeRule.personTypeName}"/>
			<tr> 
				<td>人员类型：</td>
				<td>
					<select id="personTN" name="personTN" class="easyui-combobox"
	                            data-options="width: 150,prompt:'人员类型'" value="${personTypeRule.personTypeName}">
	                            
	        		</select>
				</td>
			</tr>
			<tr>
				<td>比例：</td>
				<td><input id="ratio" name="ratio" class="easyui-validatebox" onBlur="changeaa()"  required="true" value="${personTypeRule.ratio}"></td>
			</tr>
			<tr>
				<td>备注：</td>
				<td><input id="remark" name="remark" class="easyui-validatebox"   value="${personTypeRule.remark}"></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";

$(document).ready(function(){
    selJobType("personType");
  	
});

//作业类型选择人员 
function selJobType(dataType){
	if(dataType!=""){
		$.ajax({
			type : "GET",
			async : false,
			url : "${ctx}/system/dict/json",
			data : "filter_LIKES_type="+dataType,
			dataType : "json",
			success : function(date) {
					$('#personTN').combobox({
						data : date.rows,
						value:'${personTypeRule.personTypeName}',
						valueField : 'label',
						textField : 'value',
						editable : false ,
						onChange: function (newVal, oldVal){
							
							var getV =$("#personTN").combobox('getValue');
							document.getElementById("aaaid").value=getV;
							
						}
						   
					});
			},
		});
	}else{
		$('#personTN').combobox({
						data : "",
						valueField : "",
						textField : "",
						editable : false 
					});
	}
	
}

function changeaa(){
	var getT =$("#personTN").combobox('getText');
	document.getElementById("aaaName").value=getT;
	
}

//提交表单
$('#mainform').form({    
    onSubmit: function(){ 
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){   
    	successTip(data,dg,d);
    }    
});    
</script>
</body>
</html>