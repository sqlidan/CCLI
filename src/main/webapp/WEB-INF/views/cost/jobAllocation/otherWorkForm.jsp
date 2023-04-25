<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
	<%-- <input type="hidden" id="yname" value="${client.clientName }"/>
	<input type="hidden" id="ycode" value="${client.clientCode }"/>
	<input type="hidden" id="ytax" value="${client.taxAccount }"/> --%>
	<form id="mainform" action="${ctx}/cost/piecework/${action}" method="post">
		<table class="formTable">
			<!-- <tr>
				<td>人员类型：</td>
				<td> -->
					<input type="hidden" id="id" name="id" value="${otherWorker.id}"/>
					
				<!-- 	<select id="personType" name="personType" class="easyui-combobox" data-options="width: 150 "  >
					</select>
				</td>
			</tr> -->
			<tr> 
				<td>人员：</td>
				<td>
					<select id="workerName" name="workerName" class="easyui-combobox"
	                            data-options="width: 150,prompt:'人员'" value="${otherWorker.workerName}">
	        		</select>
				</td>
			</tr>
			<tr> 
				<td>作业类型：</td>
				<td>
				<select id="workerTypeName" name="workerTypeName" class="easyui-combobox" data-options="width: 150,prompt:'作业类型'" value="${otherWorker.workerTypeName}" >
				</select>
				<!-- <select class="easyui-combobox" id="filter_LIKES_jobType" name="filter_LIKES_jobType" data-options="width:150,prompt:'作业类型'">
					<option value=""></option>
				</select> -->
				</td>
			</tr>
			<tr>
				<td>作业日期：</td>
				<td>
					<%-- <input id="ratio" name="ratio" class="easyui-validatebox"   required="true" value="${rule.ratio}">  --%>
					<input type="text" id="workDate" name="workDate" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '作业日期开始'" value="${otherWorker.workDate}"/>
				</td>
			</tr>
			<tr>
				<td>作业时长（小时）：</td>
				<td><input id="workDuration" name="workDuration" class="easyui-validatebox"   required="true" value="${otherWorker.workDuration}"></td>
			</tr>
		
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";

$(document).ready(function(){
	
	//人员 
    var manAssCC2="${person}";
    $('#workerName').combobox({
        method: "GET",
        url: "${ctx}/cost/piecework/getAllUsers?setid=${person}",
        valueField: 'person',
        textField: 'person',
        mode: 'remote',
        onLoadSuccess: function () {
             $('#workerName').combobox("select", manAssCC2);
             manAssCC2 = "";
             if(action=="updateOtherWork"){
            	 manAssCC2 ="${otherWorker.workerName}";
            	 $('#workerName').combobox("select", manAssCC2);
             }
        }
    });
	 //selPersonType();
	 //selBigType();
	 //var dataType= $("#bigType").combobox("getValue");
	 // var dataType ="jobType0";
  	// selJobType(dataType);//TODO判断是新增还是修改
    selJobType("jobType0");
  	
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
					$('#workerTypeName').combobox({
						data : date.rows,
						value : "${otherWorker.workerTypeName}",//personType
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