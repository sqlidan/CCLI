<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="底账汇总列表" style="overflow-y:auto">
<div   style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">

			<input type="text" name="filter_billNo" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_containerNo" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_appointDateStart" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '入库日期（开始）'"/>
			<input type="text" name="filter_appointDateEnd" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '入库日期（结束）'"/>

	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="report()">导出</a>
        </div>
	<table id="dg"></table> 
</div>
<script type="text/javascript">
var dg;
var d;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	gridDG();
});
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
//申请单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/baseAccountStatistics/json',
	    fit : true,
		fitColumns : false,
		border : false,
		//idField : 'forId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
		columns: [[
			{field: 'xid', title: '项号',width: 100},
			{field: 'billNo', title: '提单号',width: 150},
			{field: 'containerNo', title: '箱号',  width: 150},
			{field: 'productType', title: '货类', width: 100,
				formatter:function (value, row, index){
					if (value == "1"){
						return '水产';
					}else if (value == "2"){
						return '肉类';
					}else {
						return '其他';
					}
				}
			},
			{
				field: 'productName', title: '货物名称(品名)', width: 150},
			{field: 'rSumnum', title: '原申报数量', width: 100},
			{field: 'rSumweight', title: '原申报重量', width: 100},
			{field: 'surplusNum', title: '剩余件数',  width: 100},
			{field: 'surplusWeight', title: '剩余重量',  width: 100}
			/*{field: 'appointDate', title: '入库日期',  width: 150}*/
		]],
	    onClickRow:function(rowIndex, rowData){
	    	info(rowData.id);
	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}



//导出底账明细
function report() {
	var url = "${ctx}/supervision/baseAccountStatistics/export";
	$("#searchFrom").attr("action",url).submit();
}
</script>
</body>
</html>