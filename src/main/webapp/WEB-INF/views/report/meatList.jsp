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
       	        <input type="text" id="billNum" name="billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号',required:'required'"/>
       	        <input id="staTime" name="staTime" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt:'开始年月'"    />
       	         <input id="endTime" name="endTime" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt:'结束年月'"    />
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
        		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出EXCEL</a>
        		<span class="toolbar-item dialog-tool-separator"></span>
        </div> 
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var nhh=0;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	dg = $('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/report/stock/meatjson', 
	    fit : true,
		fitColumns : true,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field: 'TYPE',title:'记录',sortable:true,width:100,formatter : function(value, row, index) {
	        	if(value == '1'){return "入库"}else{ nhh=index ;return "出库"}
	        }},    
	        {field: 'BILL_NUM',title:'提单号',sortable:true,width:100,hidden:true},
	        {field: 'STOCK_TIME',title:'日期',sortable:true,width:100},
	        {field: 'CARGO_NAME',title:'品名',sortable:true,width:100},
	        {field: 'PIECE', title:'数量（件）', sortable:true, width:100},
	        {field: 'SUM_NET_WEIGHT', title:'重量（千克）', sortable:true, width:100},
	        {field: 'ids',title:'原产地/生产企业注册号', sortable:true,width:100},
	        {field: 'CTN_NUM', title:'集装箱号/CIQ封识号', sortable:true, width:100},
	        {field: 'STOCK_NAME', title:'入/出货单位', sortable:true, width:100},
	        {field: 'JY', title:'报检单号/通关单号', sortable:true, width:100},
	        {field: 'LAB', title:'存放库位', sortable:true, width:100}
	        
	    ]],onLoadSuccess:function(){
	    	/*var getDate = $("#dg").datagrid('getData');
			$("#dg").datagrid('mergeCells', {index:0,field:'Money',rowspan:getDate.,colspan:1});
 			if(nhh>0){
				 
			 }*/
		},
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//创建查询对象并查询
function cx(){
	var isValid = $("#searchFrom").form('validate');
	if(isValid){
		var obj=$("#searchFrom").serializeObject();
		dg.datagrid('load',obj); 	
	}
}
//导出excle
function exportExcel(){
	var isValid = $("#searchFrom").form('validate');
	if(isValid){
		 $("#searchFrom").attr("action","${ctx}/report/stock/meatexcel");
		 $("#searchFrom").submit();
	}
}

</script>
</body>
</html>