<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>     
<div>
	<form id="mainform" action="${ctx}/cost/loadingTeam/${action}" method="post">
		<table class="formTable">
		<c:if test="${action == 'update'}">
			<tr>
				<td>ID：</td>
				<td>
				    <input type="hidden" id="creater" name="creater" value="${daywork.creater}"/>
					<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${daywork.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>">
					<input type="hidden" id="delFlag" name="delFlag" value="${daywork.delFlag}"/>
					<input type="hidden" id="state" name="state" value="${daywork.state}"/>
					<input type="hidden" id="standingNum" name="standingNum" value="${daywork.standingNum}"/>
					<input id="id" name="id" class="easyui-validatebox"  data-options="width: 150 '" readonly="readonly" value="${daywork.id}"> 
				</td>
			</tr>
		</c:if>  
			<tr>
				<td>提单号：</td>
				<td>
					<input id="billNum" name="billNum" class="easyui-validatebox"  data-options="width: 150,validType:'length[1,20]'"     value="${daywork.billNum}"> 
				</td>
			</tr>
			<tr> 
				<td>装卸日期：</td>
				<td>
					<input name="loadingDate" type="text" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width: 150,required:'required'"  value="<fmt:formatDate value="${daywork.loadingDate}"/>" />
				</td>
			</tr>
			<tr> 
				<td>账单年月：</td>
				<td>
					<input name="billDate" type="text" class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,required:'required'"  value="<fmt:formatDate value="${daywork.billDate}"/>" />
				</td>
			</tr>
			<tr>
				<td>装卸队：</td>
				<td>
				    <input type="hidden" id="client" name="client">
					<select id="clientId" name="clientId" class="easyui-combobox" data-options="width: 150, required:'required'" > 
					</select>
				</td>
			</tr>
			<tr>
				<td>装卸队方案：</td>
				<td>
				    <input type="hidden" id="schemeName" name="schemeName">
				    <input type="hidden" id="feeS" name="feeS" value="${daywork.schemeId}" />
					<select id="schemeId" name="schemeId" class="easyui-combobox" data-options="width: 150, required:'required'" > 
					</select>
				</td>
			</tr>
			<tr>
				<td>小时数：</td>
				<td>
					<input id="dayworkNum" name="dayworkNum" class="easyui-validatebox"  data-options="width: 150,required:'required'"  value="${daywork.dayworkNum}"  onkeyup="ischeckNum(this)"> 
				</td>
			</tr>
			<tr>
				<td>日工备注：</td>
				<td>
					<input id="dayworkRemark" name="dayworkRemark" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]'"     value="${daywork.dayworkRemark}"> 
				</td>
			</tr>
			<tr>
				<td>电梯工数：</td>
				<td>
					<input id="evevatorNum" name="evevatorNum" class="easyui-validatebox"  data-options="width: 150"  value="${daywork.evevatorNum}" onkeyup="ischeckNum(this)"> 
				</td>
			</tr>
			<tr>
				<td>电梯工备注：</td>
				<td>
					<input id="evevatorRemark" name="evevatorRemark" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]'"     value="${daywork.evevatorRemark}"> 
				</td>
			</tr>
			<tr>
				<td>加班小时：</td>
				<td>
					<input id="overtime" name="overtime" class="easyui-validatebox"  data-options="width: 150"  value="${daywork.overtime}" onkeyup="ischeckNum(this)"> 
				</td>
			</tr>
			<tr>
				<td>加班备注：</td>
				<td>
					<input id="overtimeRemark" name="overtimeRemark" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]'"     value="${daywork.overtimeRemark}"> 
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td><textarea rows="3" cols="41" class="easyui-validatebox" data-options="validType:'length[0,50]'" name="remark" style="font-size: 12px;font-family: '微软雅黑'">${daywork.remark}</textarea></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";

//装卸队

var getstockId='${daywork.clientId}';
$('#clientId').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=2&setid=${daywork.clientId}",
			valueField: 'ids',
			textField: 'clientName',
			mode:'remote',
			onChange: function (newVal,oldVal){
							if(newVal != ""){
								feeSelect(newVal);
							}
						 },
			onLoadSuccess:function(){
					if(getstockId!=null && getstockId!=""){
						 $('#clientId').combobox("select",getstockId);
						 getstockId="";
					}
			}
});


//根据收货方获得费用方案列表
function feeSelect(newVal){
var clientId = newVal;
	$.ajax({
   		async:false,
		type : "GET",
		url : "${ctx}/wms/enterStock/selFeePlan",
		data : {"clientId":clientId},
		dataType : "json",
		success : function(date) {
		    if(date.length == 0){
				$('#schemeId').combobox({
					data : date,
					valueField : 'schemeNum',
					textField : 'schemeName',
					editable : false
				});
				 
		    }else{
					$('#schemeId').combobox({
						data : date,
						value:$("#feeS").val(),
						valueField : 'schemeNum',
						textField : 'schemeName',
						editable : false
					});
			}
		}
	});
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
$('#mainform').form({    
    onSubmit: function(){ 
		$("#client").val($('#clientId').combobox("getText"));
		$("#schemeName").val($('#schemeId').combobox("getText"));
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