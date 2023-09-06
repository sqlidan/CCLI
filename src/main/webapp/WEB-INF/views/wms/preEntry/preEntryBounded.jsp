<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="保税底账查询">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_clientName" class="easyui-validatebox" data-options="width:150,prompt: '客户名称'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_cdNum" class="easyui-validatebox" data-options="width:150,prompt: '报关单号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_EQS_customerServiceName" class="easyui-validatebox" data-options="width:150,prompt: '所属客服'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_hsCode" class="easyui-validatebox" data-options="width:150,prompt: 'hs编码'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_hsItemname" class="easyui-validatebox" data-options="width:150,prompt: '海关品名'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_accountBook" class="easyui-validatebox" data-options="width:150,prompt: '账册商品序号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
		</form>
		<form id="searchFrom3" action="">
		</form>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" data-options="disabled:false" onclick="queryHZQD()">查询</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
			   onclick="exportExcel()">导出EXCEL</a>
        </div>
	<table id="dg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg


document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	gridDG();
});

//入库报关单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/preEntryBonded/json',
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'forId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[
			{field: 'id', title: 'id', hidden:true},
			{field: 'clientId', title: '客户ID', hidden:true},
	        {field:'clientName',title:'客户名称',sortable:true,width:70},
			{field:'billNum',title:'提单号',sortable:true,width:40},
			{field:'cdNum',title:'报关单号',sortable:true,width:70},
			{field:'customerServiceName',title:'所属客服',sortable:true,width:30},
			{field:'hsCode',title:'hs编码',sortable:true,width:40},
			{field:'hsItemname',title:'海关品名',sortable:true,width:40},
			{field:'accountBook',title:'账册商品序号',sortable:true,width:20},
			{field:'dclQty',title:'申报重量',sortable:true,width:20},
			{field:'dclUnit',title:'申报计量单位',sortable:true,width:20},
			{field:'hsQty',title:'海关库存重量',sortable:true,width:20},
			{field:'typeSize',title:'规格',sortable:true,width:20},
			{field:'cargoLocation',title:'库位号',sortable:true,width:50},
			{field:'cargoArea',title:'库区',sortable:true,width:50},
			{field:'createdTime',title:'创建日期',sortable:true,width:40},
			{field:'updatedTime',title:'修改时间',sortable:true,width:40}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

function refresh(){
	window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
}

//创建查询对象并查询
function cx(){
	dg.datagrid('clearSelections');
	var obj=$("#searchFrom").serializeObject();

	dg.datagrid('load',obj); 
}
//查询
function queryHZQD(){
	parent.$.messager.prompt('提示', '请输入需要查询的核注清单号。', function(content){
		if (content){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntryBonded/getPreEntryBonded/"+content,
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
//导出excel
function exportExcel(){
	var obj=$("#searchFrom").serializeObject();
	var url = "${ctx}/wms/preEntryBonded/exportExcel";
	$("#searchFrom").attr("action",url).submit();
}


</script>
</body>
</html>