<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
	<div>
		<form id="mainform" action="${ctx}/supervision/opMani/create" method="post">
			<table class="formTable">
				<tr data-options="">
					<td>箱号：</td>
					<td><input id="ContaId" name="ContaId"
							   class="easyui-validatebox" data-options="width:150 , required:'required'"/>
					</td>
					<td>出入区类型：</td>
					<td><input id="IeFlag" name="IeFlag" class="easyui-combobox"
							   data-options=" width: 150, required:'required'"/></td>
				</tr>
				<tr>
					<td>车号：</td>
					<td><input id="VehicleId" name="VehicleId"
							   class="easyui-validatebox" data-options="width: 150, required:'required'" value="" /></td>
					<td>车重：</td>
					<td><input id="VehicleWeight" name="VehicleWeight"
							   class="easyui-validatebox"
							   data-options="width: 150, required:'required'" value="" /></td>
				</tr>
				<tr>
					<td>经营单位编码：</td>
					<td><input id="TradeCode" name="TradeCode"
							   class="easyui-validatebox" data-options="width: 150" value="3702631016" /></td>
					<td>经营单位名称：</td>
					<td><input id="TradeName" name="TradeName"
							   class="easyui-validatebox"
							   data-options="width: 150" value="青岛港怡之航冷链物流有限公司" /></td>
				</tr>
			</table>
		</form>
	</div>

	<script type="text/javascript">

		$(function() {
			resetForm();
		});

		function resetForm() {

			$("#IeFlag").combobox({
				data : [{
					'value' : 'I',
					'text' : '入区'
				}, {
					'value' : 'E',
					'text' : '出区'
				} ],
				valueField : 'value',
				textField : 'text',
				editable:false
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