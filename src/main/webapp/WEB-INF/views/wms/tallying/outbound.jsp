<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
	<div data-options="region:'center'" title="出库拣货">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<form id="searchFrom" action="">
				<input type="text" name="trayCode" class="easyui-validatebox" data-options="width:120,prompt: '托盘号'"/>
				<input type="text" name="billCode" class="easyui-validatebox" data-options="width:120,prompt: '提单号'"/>
				<input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>
				<input type="text" name="asn" class="easyui-validatebox" data-options="width:120,prompt: 'ASN'"/>
				<input type="text" name="sku" class="easyui-validatebox" data-options="width:120,prompt: 'SKU'"/>
				<input type="text" name="contactCode" class="easyui-validatebox" data-options="width:120,prompt: '联系单号'"/>
				<select id="state" name="state" class="easyui-combobox" data-options="width:120,prompt: '库存状态'">
					<option></option>
					<option value='00'>已收货</option>
					<option value='01'>已上架</option>
					<option value='10'>出库中</option>
					<option value='11'>出库理货</option>
					<option value='12'>已出库</option>
					<option value='20'>待回库</option>
					<option value='21'>回库收货</option>
					<option value='99'>报损</option>
				</select>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="clearSearch()">重置</a>
			</form>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="save()">保存</a>
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
			url:'${ctx}/wms/outbound/json',
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
				{field: 'trayCode', title: '托盘号', sortable: true, width: 100},
				{field: 'billCode', title: '提单号', sortable: true, width: 130},
				{field: 'isBonded', title: '是否一般贸易', sortable: true, width: 70,
					formatter:function(val,rowData,rowIndex){
						if(val=="0")
							return "是";
						else
							return "";
					}
				},
				{field: 'ctnNum', title: '箱号', sortable: true, width: 130},
				{field: 'bgdhdate', title: '报关单申报时间', sortable: true, width: 130},
				{field: 'createUser', title: '客服员', sortable: true, width: 130},
				{field: 'asn', title: 'ASN', sortable: true, width: 90},
				{field: 'sku', title: 'SKU', sortable: true, width: 120},
				{field: 'contactCode', title: '联系单号', sortable: true, width: 135},
				{field: 'clientName', title: '客户名称', sortable: true, width: 150},
				{field: 'warehouse', title: '仓库名', sortable: true, width: 80},
				{field: 'locationCode', title: '库位号', sortable: true, width: 80},
				{field: 'cargoName', title: '产品名称', sortable: true, width: 100},
				{field: 'nowNum', title: '现有数量', sortable: true, width: 100},
				{field: 'netWeight', title: '净重', sortable: true, width: 100},
				{field: 'grossWeight', title: '毛重', sortable: true, width: 100},
				{field: 'allpiece', title: '总数量', hidden: true},
				{field: 'allnet', title: '总净重', hidden: true},
				{field: 'allgross', title: '总毛重', hidden: true},
				{field: 'units', title: '单位', sortable: true, width: 100,
					formatter: function (value, row, index) {
						if (value == '1') {
							return '千克';
						}
						if (value == '2') {
							return '吨';
						}
					}
				},
				{field: 'state', title: '状态', sortable: true, width: 100,
					formatter: function (value, row, index) {
						if (value == '00') {
							return '已收货';
						}
						if (value == '01') {
							return '已上架';
						}
						if (value == '10') {
							return '出库中';
						}
						if (value == '11') {
							return '出库理货';
						}
						if (value == '12') {
							return '已出库';
						}
						if (value == '20') {
							return '待回库';
						}
						if (value == '21') {
							return '回库收货';
						}
						if (value == '99') {
							return '货损';
						}
					}
				},
				{field: 'enterTime', title: '入库理货时间', sortable: true, width: 130},
				{field: 'inTime', title: '入库确认时间', sortable: true, width: 130},
				{field: 'uploader', title: '转一般贸易操作人', sortable: true, width: 130},
				{field: 'uploadDate', title: '转一般贸易时间', sortable: true, width: 130},
				{field: 'enterPerson', title: '入库理货员', sortable: true, width: 100},
				{field: 'enterOp', title: '入库操作员', sortable: true, width: 100},
				{field: 'days', title: '月数差', sortable: true, width: 100}
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
	function save() {
		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		console.log("出库拣货row==>>",row)
	}
</script>
</body>
</html>