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
				<input type="text" name="filter_LIKES_bondInvtNo" class="easyui-validatebox" data-options="width:150,prompt: '理货入库'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
			</form>
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
			singleSelect:true,
			frozenColumns: [[
				{field: 'id', title: 'ID',hidden:true},
				{field: 'bondInvtNo', title: '核注清单号',sortable:true},
			]],
			columns:[[
				{field:'bizopEtpsNm',title:'经营企业名称',sortable:true},
				{field:'invtType',title:'清单类型',sortable:true,
					formatter : function(value, row, index) {
						var lbelStr;
						for (let i = 0; i < qdlxAry.length; i++) {
							let row = qdlxAry[i];
							if(row.value == value){
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}},
				{field:'dclcusFlag',title:'是否报关',sortable:true,
					formatter : function(value, row, index) {
						if(value == '1'){
							return "报关";
						}
						if(value == '2'){
							return "非报关";
						}
					}},
				{field:'dclcusTypecd',title:'报关类型代码',sortable:true,
					formatter : function(value, row, index) {
						var lbelStr;
						for (let i = 0; i < bglxAry.length; i++) {
							let row = bglxAry[i];
							if(row.value == value){
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}},
				{field:'invtDclTime',title:'清单申报时间',sortable:true},
				{field:'listStat',title:'清单状态',sortable:true,
					formatter : function(value, row, index) {
						if(value == '0'){
							return "暂存";
						}
						if(value == '1'){
							return "申报成功";
						}
						if(value == '4'){
							return "成功发送海关";
						}
						if(value == 'Q'){
							return "复审不通过";
						}
					}},
				{field:'putrecNo',title:'账册号',sortable:true},
				{field:'rcvgdEtpsNm',title:'收货企业名称',sortable:true},
				{field:'seqNo',title:'预录入统一编号',sortable:true},
				{field:'supvModecd',title:'监管方式代码',sortable:true,
					formatter : function(value, row, index) {
						var lbelStr;
						for (let i = 0; i < jgfsAry.length; i++) {
							let row = jgfsAry[i];
							if(row.value == value){
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}},
				{field:'vrfdedMarkcd',title:'核扣标记',sortable:true,
					formatter : function(value, row, index) {
						if(value == '0'){
							return "未核扣";
						}
						if(value == '1'){
							return "预核扣";
						}
						if(value == '2'){
							return "已核扣";
						}
						if(value == '3'){
							return "已核销";
						}
					}},
				{field:'entryNo',title:'报关单号',sortable:true},
				{field:'putrecNo',title:'备案编号',sortable:true},
				{field:'dclTypecd',title:'申报类型',sortable:true,
					formatter : function(value, row, index) {
						var lbelStr;
						for (let i = 0; i < sblxAry.length; i++) {
							let row = sblxAry[i];
							if(row.value == value){
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}},
				// {field:'invtIochkptStucd',title:'清单审批状态',sortable:true},
				{field:'seqNo',title:'单一窗口编号',sortable:true},
				{field:'impexpMarkcd',title:'进出口标记',sortable:true,
					formatter : function(value, row, index) {
						if(value == 'I'){
							return "进口";
						}
						if(value == 'E'){
							return "出口";
						}
					}},
				{field:'mtpckEndprdMarkcd',title:'料件成品标记',sortable:true,
					formatter : function(value, row, index) {
						if(value == 'I'){
							return "料件";
						}
						if(value == 'E'){
							return "成品";
						}
					}},
				{field:'trspModecd',title:'运输方式',sortable:true,
					formatter : function(value, row, index) {
						var lbelStr;
						for (let i = 0; i < ysfsAry.length; i++) {
							let row = ysfsAry[i];
							if(row.value == value){
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}}
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
</script>
</body>
</html>