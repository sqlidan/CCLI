<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>

<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body>

<div>
	<form id="mainform3" action="${ctx}/wms/loading/${action}" method="post"  enctype="multipart/form-data">
		<table class="formTable">
			<tr>
				<td>装车单：</td>
				<td>
					<input type="text" id="loadingTruckNum" name="loadingTruckNum" class="easyui-validatebox" data-options="width:150" value="${trayInfo.loadingTruckNum}" style="background:#eee" readonly>
				</td>
			</tr>
			<tr>
				<td>托盘号：</td>
				<td>
					<input type="text" id="trayId" name="trayId" class="easyui-validatebox" data-options="width: 150, required:'required' " value="${trayInfo.trayId}" maxlength="20" >
				</td>
			</tr>
			<tr>
				<td>品名：</td>
				<td>
					<input type="text" class="easyui-validatebox" data-options="width: 150, required:'required' " value="${trayInfo.cargoName}" maxlength="20" style="background:#eee" readonly>
				</td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
					<input type="text" class="easyui-validatebox" data-options="width: 150, required:'required' " value="${trayInfo.piece}" maxlength="20" style="background:#eee" readonly>
				</td>
			</tr>
			<tr>
				<td>月台号：</td>
				<td>
					<input type="text" id="platformNum" name="platformNum" class="easyui-validatebox" data-options="width: 150, required:'required' " maxlength="20" >
				</td>
			</tr>
			<tr>
				<td>车牌号：</td>
				<td>
					<input type="text" id="carNo" name="carNo" class="easyui-validatebox" data-options="width: 150, required:'required' " maxlength="20" >
				</td>
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
				parent.$.messager.show({title: "提示", msg: "拣货成功！", position: "bottomRight"});
				gridDG();
				d.panel('close');
			} else {
				alert(res);
				d.panel('close');
			}
		}
	});
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
</script>
</body>
</html>