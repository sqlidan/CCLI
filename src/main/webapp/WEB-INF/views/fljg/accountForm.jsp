<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/supervision/baseAccountInfo/${action}" method="post" >
		<table class="formTable">
		
			 <tr>
				<td>id：</td>
				<td>
					<input id="id" readonly style="background:#eee" name="id" class="easyui-validatebox" value="${account.id}" data-options="width: 150 ,validType:'length[0,10]'"    >
				</td>
			</tr>
			 <tr>
				<td>件数：</td>
				<td>
					<input id="num" required="true" name="num" class="easyui-validatebox" value="${account.num}"  data-options="width: 150 ,required:'required' ,number:'true',validType:'length[0,10]'"    >
				</td>
			</tr>
			<tr>
				<td>数量：</td>
				<td>
					<input id="weight" required="true" name="weight" class="easyui-validatebox"  value="${account.weight}"  data-options="width: 150,required:'required' ,number:'true',validType:'length[0,10]'"  >
				</td>
			</tr>
			
		</table>
	</form>
</div>

<script type="text/javascript">
 







 
</script>
</body>
</html>