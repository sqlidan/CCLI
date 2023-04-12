<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/wms/gate/addplat" method="post">
		<table class="formTable">
			<tr>
				<td>月台口：</td>
				<td>
					<select id="platform" name="platform" class="easyui-combobox" data-options="width:240">
					</select> 
				</td>
				<td>
					<input type='hidden' name="id" value="${car.id}">
					</input> 
				</td>
			</tr>
			<tr>
				<td>月台口2：</td>
				<td>
					<select id="platformtwo" name="platformtwo" class="easyui-combobox" data-options="width:240">
					</select> 
				</td>
				<td>
					<input type='hidden' name="id" value="${car.id}">
					</input> 
				</td>
			</tr>
		
		</table>
	</form>
</div>

<script type="text/javascript">
$(document).ready(function(){
   $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/base/platform/json",
            dataType: "json",
            success: function (date) {
                $('#platform').combobox({
                    data: date.rows,
                    value: '${car.platform}',
                    valueField: 'platform',
                    textField: 'platform',
                    editable: false
                });
                $('#platformtwo').combobox({
                    data: date.rows,
                    value: '',
                    valueField: 'platform',
                    textField: 'platform',
                    editable: false
                });
            }
        });
});
//提交表单
$('#mainform2').form({    
    onSubmit: function(){ 
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){   
    	successTip(data,dgg,d);
    }    
});    
</script>
</body>
</html>