<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
                <input type="text" name="searchStrTime" class="easyui-my97" datefmt="yyyy-MM"   data-options="width:150,prompt: '开始年月'"/>
	            <input type="text" name="searchEndTime" class="easyui-my97" datefmt="yyyy-MM"   data-options="width:150,prompt: '结束年月'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
	       	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="dc()">导出Excel</a>
	       	    <span class="toolbar-item dialog-tool-separator"></span>
			</form>
        </div> 
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

//客户
	   $('#clientName').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});

$(function(){   
	dg = $('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/report/outStock/json', 
	    fit : true,
		fitColumns : true,
		border : false,
// 		idField : 'trayCode',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field: 'itemNum',title:'项号',sortable:true,width:100},    
	        {field: 'cargoName',title:'品名',sortable:true,width:100},
	        {field: 'declareTime',title:'日期',sortable:true,width:130},
	        {field: 'cdNum',title:'报关单号',sortable:true,width:100},
	        {field: 'netWeight', title:'净重（千克）', sortable:true, width:100},
	        {field: 'totalPrices', title:'货值（美元）', sortable:true, width:100},
	        {field: 'duty',title:'关税', sortable:true,width:100},
	        {field: 'zzs', title:'增值税', sortable:true, width:100},
	        {field: 'consignee', title:'流向', sortable:true, width:100},
	        {field: 'tradeType', title:'贸易方式', sortable:true, width:100}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
function dc(){
	var url = "${ctx}/report/outStock/exportCustomsOutStockExcel";
 	$("#searchFrom").attr("action",url).submit();
}
</script>
</body>
</html>