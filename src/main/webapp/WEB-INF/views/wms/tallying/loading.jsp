<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
	<div data-options="region:'center'" title="装车理货">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<form id="searchFrom" action="">
				<input type="text" id="loadingPlanNum" name="filter_LIKES_loadingPlanNum" class="easyui-validatebox"
					   data-options="width:150,prompt: '订单号'"/>
				<input type="text" id="loadingTruckNum" name="filter_LIKES_loadingTruckNum" class="easyui-validatebox"
					   data-options="width:150,prompt: '装车单号'"/>
				<input type="text" id="outLinkId" name="filter_LIKES_outLinkId" class="easyui-validatebox"
					   data-options="width:150,prompt: '出库联系单号'"/>
				<input type="text" id="billNum" name="filter_LIKES_billNum" class="easyui-validatebox"
					   data-options="width:150,prompt: '提单号'"/>
				<input type="text" id="carNo" name="filter_LIKES_carNo" class="easyui-validatebox"
					   data-options="width:150,prompt: '车牌号'"/>
				<input type="text" id="sku" name="filter_LIKES_skuId" class="easyui-validatebox"
					   data-options="width:150,prompt: 'SKU'"/>
				<select type="text" id="loadingState" name="filter_LIKES_loadingState"
						class="easyui-combobox" data-options="width:150,prompt: '状态'">
					<option></option>
					<option value='0'>已分配</option>
					<option value='1'>已拣货</option>
					<option value='2'>已装车</option>
					<option value='5'>回库理货</option>
					<option value='6'>已回库</option>
				</select>

				<input type="text" id="operator" name="filter_LIKES_operator" class="easyui-validatebox"
					   data-options="width:150,prompt: '客服人员'"/>
				<select id="searchStock" name="filter_LIKES_stockId" class="easyui-combobox"
						data-options="width:150,prompt: '存货方'">
				</select>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="clearSearch()">重置</a>
			</form>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="save()">理货确认</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="back()">回库</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</div>
	</div>
</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
	var dg;
	var d;
	var dgg

	document.onkeydown = function () {if(event.keyCode == 13){cx();}};

	$(function(){
		gridDG();
	});

	function gridDG(){
		dg=$('#dg').datagrid({
			method: "get",
			url:'${ctx}/wms/loading/json',
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
			singleSelect:false,
			frozenColumns: [[
				{field: 'id', title: 'ID',hidden:true},
			]],
			columns:[[
				{field: 'trayId', title: '托盘号', sortable: false, width: 100},
				{field: 'outLinkId', title: '出库联系单', sortable: false, width: 180},
				{field: 'loadingPlanNum', title: '订单号', sortable: false, width: 150},
				{field: 'loadingTruckNum', title: '装车号', sortable: false, width: 150},
				{
					field: 'loadingState', title: '装车状态', sortable: false, formatter: function (value, row, index) {
						var retStr = "";
						switch (value) {
							case '1':
								retStr = "已拣货";
								break;
							case '2':
								retStr = "已装车";
								break;
							case '3':
								retStr = "已置换";
								break;
							case '0':
								retStr = "已分配";
								break;
							case '4':
								retStr = "待回库";
								break;
							case '5':
								retStr = "回库理货";
								break;
							case '6':
								retStr = "已回库";
								break;
						}
						return retStr;
					}
				},
				{field: 'loadingTime', title: '装车时间', sortable: false, width: 180},
				{
					field: 'stockId', title: '存货方', sortable: false, width: 200,
					formatter: function (value, row, index) {
						var aa = "";
						$.ajax({
							async: false,
							type: 'get',
							url: "${ctx}/base/client/getname/" + value,
							success: function (data) {
								aa = data;
							}
						});
						return aa;
					}
				},
				{field: 'mscNum', title: 'msc号', sortable: false, width: 100},
				{field: 'lotNum', title: 'lot号', sortable: false, width: 100},
				{field: 'typeSize', title: '规格', sortable: false, width: 100},
				{field: 'carNo', title: '车牌号', sortable: false, width: 100},
				{field: 'operator', title: '客服人员', sortable: false, width: 100},
				{field: 'cargoName', title: '品名', sortable: false, width: 200},
				{field: 'piece', title: '件数', sortable: false, width: 100},
				{field: 'cargoLocation', title: '库位号', sortable: false, width: 100},
				{field: 'ctnNum', title: '箱号', sortable: false, width: 100},
				{field: 'skuId', title: 'SKU', sortable: false, width: 180},
				{field: 'loadingPerson', title: '理货员', sortable: false, width: 100},
				{field: 'libraryManager', title: '库管员', sortable: false, width: 100},
				{
					field: 'enterState', title: '货物状态', sortable: false, formatter: function (value, row, index) {
						if ("1" == value) {
							return "货损"
						} else if ("0" == value) {
							return "成品"
						}
					}
				}
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
	//清空搜索条件
	function clearSearch(){
		dg.datagrid('clearSelections');
		$('#searchFrom').form('clear');
	}
	// =================================================================================
	//理货确认
	function save() {

		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		var rows = dg.datagrid('getSelections');
		if (rows.length > 1){
			parent.$.messager.show({title: "提示", msg: "请选择一行数据！", position: "bottomRight"});
			return;
		}
		d = $("#dlg").dialog({
			title: "理货确认",
			width: 450,
			height: 450,
			href: '${ctx}/wms/loading/loading/' + row.id,
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
	//回库
	function back() {
		var rows = dg.datagrid('getSelections');
		var del = dg.datagrid('getSelected');
		if (del == null) {
			parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
			return;
		}
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		parent.$.messager.confirm('提示', '您确定要将选中数据进行回库吗？', function (data) {
			if (data) {
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/loading/back/" + ids,
					success: function (data) {
						dg.datagrid('clearSelections');
						successTip(data, dg);
					}
				});
			}
		});
	}
</script>
</body>
</html>