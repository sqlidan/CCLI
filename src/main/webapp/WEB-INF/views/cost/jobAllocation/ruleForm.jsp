<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
	<input type="hidden" id="yname" value="${client.clientName }"/>
	<input type="hidden" id="ycode" value="${client.clientCode }"/>
	<input type="hidden" id="ytax" value="${client.taxAccount }"/>
	<form id="mainform" action="${ctx }/cost/piecework/${action}" method="post">
		<table class="formTable">
			<!-- <tr>
				<td>人员类型：</td>
				<td> -->
					<input type="hidden" id="id" name="id" value="${rule.id}"/>
					<input type="hidden" id="keyId" name="keyId" value="${rule.keyId}"/>
				<!-- 	<select id="personType" name="personType" class="easyui-combobox" data-options="width: 150 "  >
					</select>
				</td>
			</tr> -->
			<tr> 
				<td>作业大类：</td>
				<td>
					<select id="bigType"  name="bigType" class="easyui-combobox" data-options="width: 150"  >
					</select>
				</td>
			</tr>
			<tr> 
				<td>作业类型：</td>
				<td>
				<select id="jobType" name="jobType" class="easyui-combobox" data-options="width: 150"  >
				</select>
				</td>
			</tr>
			<tr>
				<td>比例：</td>
				<td>
					<input id="ratio" name="ratio" class="easyui-validatebox"   required="true" value="${rule.ratio}"> 
				</td>
			</tr>
			<tr>
				<td>录入员</td>
				<c:choose>
					<c:when test="${action == 'create'}">
						<td><input id="operator" name="operator" value="${user}" class="easyui-validatebox" data-options="width: 150"  disabled/></td>
					</c:when>
					<c:otherwise>
						<td><input id="operator" name="operator" value="${rule.operator}" class="easyui-validatebox" data-options="width: 150"  disabled/></td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>录入时间</td>
				<c:choose>
					<c:when test="${action == 'create'}">
						<td><input id="operateTime" name="operateTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 150" value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/></td>
					</c:when>
					<c:otherwise>
						<td><input id="operateTime" name="operateTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 150" value="<fmt:formatDate value="${rule.operateTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/></td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>备注：</td>
				<td><textarea id="remark" name="remark" rows="3" cols="41" class="easyui-validatebox" data-options="validType:'length[0,50]'" name="note" style="font-size: 12px;font-family: '微软雅黑'">${rule.remark}</textarea></td>
			</tr>
		
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";


$(document).ready(function(){
	 //selPersonType();
	 selBigType();
	 var dataType= $("#bigType").combobox("getValue");
  	 selJobType(dataType);
});

//作业类型选择
function selJobType(dataType){
	if(dataType!=""){
		$.ajax({
			type : "GET",
			async : false,
			url : "${ctx}/system/dict/json",
			data : "filter_LIKES_type="+dataType,
			dataType : "json",
			success : function(date) {
					$('#jobType').combobox({
						data : date.rows,
						value : "${rule.jobType}",//personType
						valueField : 'value',
						textField : 'label',
						editable : false 
					});
			},
		});
	}else{
		$('#jobType').combobox({
						data : "",
						valueField : "",
						textField : "",
						editable : false 
					});
	}
}

//人员类型选择
/* function selPersonType(){
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=personType",
		dataType : "json",
		success : function(date) {
				$('#personType').combobox({
					data : date.rows,
					value : "${rule.personType}",
					valueField : 'label',
					textField : 'label',
					editable : false
				});
		}
	});
} */

//作业大类选择
function selBigType(){
	var bt='${rule.bigType}';
	if(null==bt || bt=="undefined"){
		bt=1
	}
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=bigType",
		dataType : "json",
		success : function(date) {
				$('#bigType').combobox({
					data : date.rows,
					value : bt,
					valueField : 'value',
					textField : 'label',
					editable : false,
					onChange: function (newVal, oldVal){
					   selJobType(newVal);
				   }
				});
		}
		
	});
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