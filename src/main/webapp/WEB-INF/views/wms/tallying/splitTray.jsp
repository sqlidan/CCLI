<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
	<div data-options="region:'center'" title="拆托">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<form id="searchFrom" action="">
				<input type="text" id="asn" name="filter_LIKES_asn" class="easyui-validatebox" data-options="width:150,prompt: 'ASN'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<input type="text" id="skuId" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150,prompt: 'SKU'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<input type="text" id="billNum" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<input type="text" id="ctnNum" name="filter_LIKES_ctnNum" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<input type="text" id="trayId" name="filter_LIKES_trayId" class="easyui-validatebox" data-options="width:150,prompt: '托盘号'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="clearSearch()">重置</a>
			</form>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="splitTray()">拆托</a>
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
			url:'${ctx}/wms/splitTray/json',
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
				{field:'asn',title:'ASN',sortable:true},
				{field:'skuId',title:'SKU',sortable:true},
				{field:'billNum',title:'提单号',sortable:true},
				{field:'ctnNum',title:'箱号',sortable:true},
				{field:'trayId',title:'托盘号',sortable:true},
				// {field:'cargoType',title:'产品类型',sortable:true},
				{field:'cargoName',title:'产品名称',sortable:true},
				{field:'warehouse',title:'仓库号',sortable:true},
				{field:'buildingNum',title:'楼号',sortable:true},
				{field:'floorNum',title:'楼层号',sortable:true},
				{field:'RoomNum',title:'房间号',sortable:true},
				{field:'areaNum',title:'区位号',sortable:true},
				{field:'actualStoreroomX',title:'X坐标',sortable:true},
				{field:'actualStoreroomZ',title:'Z坐标',sortable:true},
				{field:'originalPiece',title:'初始件数',sortable:true},
				{field:'nowPiece',title:'现有件数',sortable:true},
				{field:'qty',title:'件数',sortable:true},
				{field:'removePiece',title:'拆托数量',sortable:true},
				{field:'enterRemark2',title:'修改上架件数备注',sortable:true},
			]],
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar:'#tb'
		});
	}

	//搜索
	function cx(){
		dg.datagrid('clearSelections');
		var obj=$("#searchFrom").serializeObject();
		dg.datagrid('load',obj);
	}
	// =================================================================================
	//拆托
	function splitTray() {
		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		console.log(" row.id==>>"+ row.id)
		d = $("#dlg").dialog({
			title: "拆托操作",
			width: 450,
			height: 450,
			href: '${ctx}/wms/splitTray/splitTray/' + row.id,
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
	//清空搜索条件
	function clearSearch(){
		$('#searchFrom').form('clear');
		dg.datagrid('clearSelections');
	}
</script>
</body>
</html>