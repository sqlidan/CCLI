<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>

<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body>

<div>
	<form id="mainform3" action="${ctx}/wms/tallying/${action}" method="post"
		  enctype="multipart/form-data">
		<table class="formTable">
			<tr>
				<td><input name="file" type="file" extend="*.xls;*.xlsx"></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
	//提交表单
	$('#mainform3').form({
		onSubmit: function () {
			return true;	// 返回false终止表单提交
		},
		success: function (res) {
			if (res == "success") {
				parent.$.messager.show({title: "提示", msg: "导入预览成功！", position: "bottomRight"});
				gridDG();
				d.panel('close');
			} else {
				var mess = "EXCEL中第" + res + "条数据的异常，无法导入!";
				alert(mess);
				d.panel('close');
			}
		}
	});
</script>
</body>
</html>