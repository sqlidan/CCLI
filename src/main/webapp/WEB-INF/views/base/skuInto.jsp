<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx }/base/sku/${action}" method="post" enctype="multipart/form-data">
		<table class="formTable">
			<tr>
				<td><input name="file" type="file" extend="*.xls;*.xlsx"></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
//提交表单

$('#mainform').form({    
    onSubmit: function(){    
		return true;	// 返回false终止表单提交
    },    
    success:function(data){   
    	successTip(data,dg,d);
    }    
});    
</script>
</body>
</html>