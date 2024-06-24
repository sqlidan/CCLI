<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
	<div data-options="region:'center'" title="移库">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<form id="searchFrom" action="">
				<input type="text" name="filter_LIKES_trayId" class="easyui-validatebox" data-options="width:150,prompt: '托盘号'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
			</form>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="moveTray()">移库</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</div>
	</div>
</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
	var dg;
	var d;

	document.onkeydown = function () {if(event.keyCode == 13){cx();}};

	$(function(){
		gridDG();
	});

	function gridDG(){
		dg=$('#dg').datagrid({
			method: "get",
			url:'${ctx}/wms/relocation/json',
			fit : true,
			fitColumns : true,
			border : false,
			idField : 'id',
			sortOrder:'desc',
			striped:true,
			pagination:true,
			rownumbers:true,
			pageNumber:1,
			pageSize : 20,
			pageList : [20, 30, 50, 100 ],
			singleSelect:true,
			frozenColumns: [[
				{field: 'id', title: 'ID',hidden:true},
			]],
			columns:[[
				{field:'trayId',title:'托盘号',sortable:true},
				{field:'ctnNum',title:'箱号',sortable:true},
				{field:'billNum',title:'提单号',sortable:true},
				{field:'cargoLocation',title:'货位',sortable:true},
				{field:'asn',title:'ASN',sortable:true},
				{field:'skuId',title:'SKU',sortable:true},
				{field:'skuId',title:'品名',sortable:true},
				{field:'actualStoreroomX',title:'X坐标',sortable:true},
				{field:'actualStoreroomZ',title:'Z坐标',sortable:true}
			]],
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar:'#tb'
		});
	}

	//搜索
	function cx(){
		var obj=$("#searchFrom").serializeObject();
		dg.datagrid('load',obj);
	}
	// =================================================================================
	//移库
	function moveTray() {
		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		console.log(" row.id==>>"+ row.id)
		d = $("#dlg").dialog({
			title: "移库操作",
			width: 450,
			height: 450,
			href: '${ctx}/wms/relocation/moveTray/' + row.id,
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