<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
	<div data-options="region:'center'" title="核注清单查询">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<form id="searchFrom" action="">
				<input type="text" name="filter_LIKES_bondInvtNo" class="easyui-validatebox" data-options="width:150,prompt: '核注清单号'"/>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
			</form>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" data-options="disabled:false" onclick="queryHZQD()">查询核注清单</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="ck()">查看核注清单详情</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<shiro:hasPermission name="wms:preEntryInvtQuery:synchronization">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="synchronization()">同步核注清单数据</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:preEntryInvtQuery:createPreEntry">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="createPreEntry()">批量生成预报单</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
        </div>
	</div>
</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
var dg;
var d;
var dgg

var qdlxAry;//清单类型
var bglxAry;//报关类型代码
var jgfsAry;//监管方式代码
var sblxAry;//申报类型
var ysfsAry;//运输方式
var qdshztAry;//清单审批状态

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	gridDG();
	selectAjax();
});
function selectAjax(){
	//清单类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_INVT_TYPE",
		success : function(date) {
			qdlxAry = date.rows;
		}
	});
	//报关类型代码
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCLCUSFLAG",
		success : function(date) {
			bglxAry = date.rows;
		}
	});
	//监管方式
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_SUPVMODECD",
		success : function(date) {
			jgfsAry = date.rows;
		}
	});
	//申报类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCLTYPECD",
		success : function(date) {
			sblxAry = date.rows;
		}
	});
	//运输方式
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_TRSPMODECD",
		success : function(date) {
			ysfsAry = date.rows;
		}
	});
}
//入库报关单列表
function gridDG(){
	dg=$('#dg').datagrid({
		method: "get",
	    url:'${ctx}/wms/preEntryInvtQuery/json',
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
			{field: 'id', title: 'ID',sortable:true},
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
					if(value == '5'){
						return "海关接收成功";
					}
					if(value == '6'){
						return "海关接收失败";
					}
					if(value == 'B'){
						return "海关终审通过";
					}
					if(value == 'C'){
						return "退单";
					}
					if(value == 'E'){
						return "删除";
					}
					if(value == 'T'){
						return "转人工";
					}
					if(value == 'N'){
						return "待导入其他报文";
					}
					if(value == 'P'){
						return "预审核通过";
					}
					if(value == 'R'){
						return "待复审";
					}
					if(value == 'Y'){
						return "复审通过";
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
				}},
			{field:'gdsSeqno',title:'商品序号',sortable:true},
			{field:'dclQty',title:'申报数量',sortable:true},
			{field:'corrEntryDclEtpsNm',title:'申报单位',sortable:true},
			{field:'gdsMtno',title:'商品料号',sortable:true},
			{field:'bizopEtpsno',title:'经营企业编号',sortable:true},
			{field:'dclEtpsNm',title:'申报企业名称',sortable:true},
			{field:'etpsInnerInvtNo',title:'企业内部编号',sortable:true},
			{field:'createBy',title:'创建人',sortable:true},
			{field:'createTime',title:'创建日期',sortable:true},
			// {field:'updateBy',title:'修改人',sortable:true},
			// {field:'updateTime',title:'修改时间',sortable:true}
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
//查询核注清单信息
function queryHZQD(){
	parent.$.messager.prompt('提示', '请输入需要查询的核注清单号。', function(content){
		if (content){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntryInvtQuery/getPreEntryInvtQuery/"+content,
				success: function(data){
					successTip(data,dg);
				},
			});
		}else{
			parent.$.messager.show({ title : "提示",msg: "请输入核注清单号！", position: "bottomRight" });
			return;
		}
	});
}
//同步核注清单数据
function synchronization(){
	parent.$.messager.confirm('提示', '确定进行核注清单的数据同步吗？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntryInvtQuery/synchronizationInvtQuery",
				success: function(data){
					successTip(data,dg);
				},
			});
		}else{
			return;
		}
	});
}
//批量生成预报单
function createPreEntry(){
	parent.$.messager.confirm('提示', '确定批量生成预报单吗？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntryInvtQuery/createPreEntry",
				success: function(data){
					successTip(data,dg);
				},
			});
		}else{
			return;
		}
	});
}
//查看
function ck(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('核注清单详情','wms/preEntryInvtQuery/invtDetail/' + row.id);
}
</script>
</body>
</html>