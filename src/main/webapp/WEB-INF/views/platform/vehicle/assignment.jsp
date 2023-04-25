<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx }/platform/vehicle/queue/${action}" method="post">
		<table class="formTable">
			<tr>


					<input type="hidden" name="id" value="${queueId }"/>
					<select id="platformId" name="platformId" class="easyui-combobox"
						data-options="width:100,prompt: '请选择指派月台'">
						<option value=""></option>
					</select>

			</tr>
		</table>
	</form>
</div>
    <%--todo 改月台下拉框请求数据 --%>
<script type="text/javascript">
var action="${action}";
$.ajax({
	type : "GET",
	async : false,
	url : "${ctx}/platform/station/maintenance/json",
	data : {"trayRoomNum":'${trayRoomNum}',"inoutBoundFlag":'${inoutBoundFlag}'},
	dataType : "json",
	success : function(data) {
		for(var i=0; i<data.rows.length; i++){
			$('#platformId').combobox({
				data: data.rows,
				valueField: 'id',
				textField: 'platform',
				editable : false
			});
		}
	}
});


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