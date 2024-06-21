<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>

<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body>

<div>
	<form id="mainform3" action="${ctx}/wms/splitTray/${action}" method="post"  enctype="multipart/form-data">
		<table class="formTable">
			<tr>
				<input  type="hidden" id="id" name="id" value="${trayInfo.id}">
				<td>原托盘号：</td>
				<td>
					<input class="easyui-validatebox" data-options="width:150" value="${trayInfo.trayId}" readonly>
				</td>
			</tr>
			<tr>
				<td>新托盘号：</td>
				<td>
					<input type="text" id="trayId" name="trayId" class="easyui-validatebox" data-options="width: 150, required:'required' " maxlength="20" >
				</td>
			</tr>
			<tr>
				<td>拆出件数：</td>
				<td>
					<input type="text" id="qty" name="qty" class="easyui-validatebox"  data-options="width: 150, required:'required'" value="0" maxlength="10" oninput="ischeckNum(this)" onpropertychange="ischeckNum(this)">
				</td>
			</tr>
			<tr>
				<td>原托盘件数：</td>
				<td><input type="text" class="easyui-validatebox"  data-options="width: 150 " value="${trayInfo.nowPiece}" readonly></td>
			</tr>
			<tr>
				<td>SKU：</td>
				<td><input  type="text" class="easyui-validatebox"  data-options="width: 150 " value="${trayInfo.skuId}" readonly></td>
			</tr>
			<tr>
				<td>品名：</td>
				<td><input type="text" class="easyui-validatebox"  data-options="width: 150 " value="${trayInfo.cargoName}" readonly></td>
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
				parent.$.messager.show({title: "提示", msg: "拆托成功！", position: "bottomRight"});
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