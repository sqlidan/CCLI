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
		<form id="mainform" action="${ctx}/supervision/mani/out/save"
			method="post">
			<table class="formTable">
				<tr data-options="">
					<td>集装箱号：</td>
					<td><input id="ContaId" name="ContaId"
						class="easyui-validatebox" value="${ContaId}"
						style="background: #eee" data-options="width:150 " readonly />
					</td>
					<td>毛重：</td>
					<td><input id="GrossWt" name="GrossWt"
						class="easyui-validatebox" value="${GrossWt}"
						style="background: #eee" data-options="width:150 " readonly />
					</td>
				</tr>
				<tr>
					<td>车辆号码：</td>
					<td><input id="VehicleId" name="VehicleId"
						class="easyui-validatebox" data-options="width: 150" value="${VehicleId}" 
						style="background: #eee" readonly/></td>
					<td>车辆自重：</td>
					<td><input id="VehicleWeight" name="VehicleWeight"
						class="easyui-validatebox" data-options="width: 150, required:'required'" value="" /></td>
				</tr>
				<tr>
				<tr>
					<td><input id="InfoIds" name="InfoIds" type="hidden"
						class="easyui-validatebox" value="${OrderNum}"
						style="background: #eee" data-options="width:150 " readonly />
					</td>
					 <td><input id="ctnTypeSize" name="ctnTypeSize" type="hidden"
						class="easyui-validatebox" value="${ctnTypeSize}"
						style="background: #eee" data-options="width:150 " readonly />
					</td>
				</tr>
			</table>
			<table class="formTable">
				<tr>
					<td>申请备注：</td>
					<td><textarea id="DNote" name="DNote" rows="3" cols="41"
							class="easyui-validatebox"
							data-options="validType:'length[0,50]'"
							style="font-size: 12px; font-family: '微软雅黑'" /></td>
				</tr>
	
			</table>
		</form>
	</div>

	<script type="text/javascript">
		//var list = '${bisEnterStockInfo}';
		//提交表单
		$('#mainform').form({    
		    onSubmit: function(){    
		    	var isValid = $(this).form('validate');
				return isValid;	// 返回false终止表单提交
		    },    
		    success:function(data){
		    	if(data == "success"){
		    		successTip(data,dg,d);
		    	}else{
		    		$.messager.alert(data);
		    	}
		    	
		    }    
		}); 
	</script>
</body>
</html>