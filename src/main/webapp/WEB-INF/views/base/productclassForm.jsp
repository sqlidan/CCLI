<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/base/product/${action}" method="post" >
		<table class="formTable">
		
			 <tr>
				<td>id：</td>
				<td>
					<input id="id" readonly style="background:#eee" name="id" class="easyui-validatebox" value="${product.id}" data-options="width: 150 ,validType:'length[0,10]'"    > 
				</td>
			</tr>
			 <tr>
				<td>小类名：</td>
				<td>
					<input id="pName" required="true" name="pName" class="easyui-validatebox" value="${product.pName}"  data-options="width: 150 ,validType:'length[0,10]'"    > 
				</td>
			</tr>
			<tr>
				<td>父id：</td>
				<td>
					<input id="printId" required="true" name="printId" class="easyui-validatebox"  value="${product.printId}"  data-options="width: 150,required:'required' ,number:'true',validType:'length[0,10]'"  > 
				</td>
			</tr>
			
		</table>
	</form>
</div>

<script type="text/javascript">
 







 
</script>
</body>
</html>