<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'">
	<form id="downForm" method="post"></form>
<%--	<form id="mainForm" method="post">--%>
<%--		<div style="height: auto" class="datagrid-toolbar">--%>
<%--				<a href="javascript:void(0)" class="easyui-linkbutton"--%>
<%--				   iconCls="icon-standard-disk" plain="true"--%>
<%--				   data-options="disabled:false" onclick="submitForm()">保存</a>--%>
<%--				<span class="toolbar-item dialog-tool-separator"></span>--%>
<%--		</div>--%>
<%--		<table class="formTable">--%>
<%--			<tr>--%>
<%--				<td>ASN</td>--%>
<%--				<td>--%>
<%--					<input id="asn" name="asn" class="easyui-validatebox" data-options="width: 180"/>--%>
<%--				</td>--%>
<%--				<td>SKU</td>--%>
<%--				<td>--%>
<%--					<input id="sku" name="sku" class="easyui-validatebox" data-options="width: 180"/>--%>
<%--				</td>--%>
<%--			</tr>--%>
<%--		</table>--%>
<%--	</form>--%>
<%--</div>--%>
<%--<div data-options="region:'south',split:true,border:false" title="托盘明细" style="height: 600px">--%>
	<div id="tb" style="padding: 5px; height: auto"
		 class="datagrid-toolbar">
		<div>
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   iconCls="icon-save" plain="true" onclick="submitForm()">保存</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   iconCls="icon-standard-basket-put" plain="true"
			   data-options="disabled:false" onclick="down()">下载导入预览模板</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   iconCls="icon-standard-basket-put" plain="true"
			   data-options="disabled:false" onclick="intoc()">EXCEL导入预览</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</div>
	</div>
	<table id="dg"></table>
	<div id="dlg"></div>
</div>

<script type="text/javascript">
	var dg;
	var d;

	function gridDG() {
		dg = $('#dg').datagrid({
			method: "get",
			url:'${ctx}/wms/tallying/json',
			fit: true,
			fitColumns: false,
			border: false,
			striped: true,
			pagination: true,
			rownumbers: true,
			pageNumber: 1,
			pageSize: 20,
			pageList: [10, 20, 50, 100, 200, 500],
			singleSelect: false,
			columns: [[
				{field: 'asn', title: 'ASN', sortable: true, width: 150},
				{field: 'sku', title: 'SKU', sortable: true, width: 150},
				{field: 'tallyNo', title: '托盘号', sortable: true, width: 150},
				{field: 'state', title: '状态', sortable: true, width: 150},
				{field: 'num', title: '数量', sortable: true, width: 150}
			]],
			// onLoadSuccess: function () {
			// 	insertSum();
			// },
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar: '#tb2'
		});

	}

	//保存
	function submitForm() {
		var data = dg.datagrid('getData');
		var rows = data.rows;
		if(rows.length == 0) {
			parent.$.messager.show({title: "提示", msg: "请导入托盘数据！", position: "bottomRight" });
			return;
		}
		//用ajax提交form
		$.ajax({
			async: false,
			type: 'POST',
			url: "${ctx}/wms/tallying/save",
			data: JSON.stringify(rows),
			contentType: 'application/json;charset=utf-8',
			success: function (msg) {
				if (msg == "success") {
					parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
					gridDG();
				}
			}
		});
	}

	//删除托盘号
	function delInfo(){
		var data = dg.datagrid('getData');
		var rows = data.rows;
		var del = dg.datagrid('getSelected');
		if(del == null) {
			parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
			return;
		}
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if (data){
				for(var i=0; i<rows.length; i++){
					if(rows[i].tallyNo = del.tallyNo){
						rows.remove(del)
					}
				}
				$('#dg').datagrid('loadData', rows);
			}
		});
	}

	//下载
	function down() {
		var url = "${ctx}/wms/tallying/download";
		$("#downForm").attr("action", url).submit();
	}

	//导入
	function intoc() {
		d = $("#dlg").dialog({
			title: "入库理货托盘明细导入",
			width: 450,
			height: 450,
			href: '${ctx}/wms/tallying/into',
			maximizable: true,
			modal: true,
			buttons: [{
				text: '确认',
				handler: function () {
					$("#mainform3").submit();
				}
			}, {
				text: '取消',
				handler: function () {
					d.panel('close');
				}
			}]
		});
	}
</script>
</body>
</html>