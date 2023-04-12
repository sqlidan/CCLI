<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>添加搬运工</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx }/cost/stevedore/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>姓名：</td>
				<td>
					<input type="hidden" name="id" value="${id }"/>
					<input name="createDate" type="hidden" data-options="width: 150" value="<fmt:formatDate value="${stevedore.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					<input id="name" name="name" class="easyui-validatebox" data-options="width: 150" value="${stevedore.name }">
				</td>
			</tr>

			<tr>
				<td>公司</td>
				<td>
					<select id="companyId" name="companyId" class="easyui-combobox"
							data-options="width:150">
					</select>
				</td>
			</tr>


			<tr>
				<td>性别：</td>
				<td>
				<input type="radio" id="man" name="gender" value="1"/><label for="man">男</label>
				<input type="radio" id="woman" name="gender" value="0"/><label for="woman">女</label>
				</td>
			</tr>
			<tr>
				<td>电话：</td>
				<td><input type="text" name="phone" value="${stevedore.phone }" class="easyui-numberbox"  data-options="width: 150"/></td>
			</tr>
			<tr>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
//用户 添加修改区分
if(action=='create'){
	$("input[name='gender'][value=1]").attr("checked",true); 
	//用户名存在验证
	$('#name').validatebox({
	    required: true,    
	    validType:{
	    	length:[2,20],
	    	remote:["${ctx}/cost/stevedore/checkName","name"]
	    }
	});  
}else if(action=='update'){
	$("input[name='name']").attr('readonly','readonly');
	$("input[name='name']").css('background','#eee')
	$("input[name='gender'][value=${stevedore.gender}]").attr("checked",true);
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


var companyId = '${stevedore.companyId}';
$('#companyId').combobox({
	method: "GET",
	url: "${ctx}/base/client/getClientList?filter_EQS_clientSort=2",
	valueField: 'ids',
	textField: 'clientName',
	mode: 'remote',
	onLoadSuccess: function () {
		if (companyId != null && companyId != "") {
			$('#companyId').combobox("select", companyId);
			companyId = "";
		}
	}
});



</script>
</body>
</html>