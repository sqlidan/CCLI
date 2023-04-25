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
       	        <input id="starTime" name="starTime" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt:'开始年月',required:'required'"    />
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
	    url:'${ctx}/report/stock/fisherjson', 
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
	    columns:[
	        [
	         {"title":"入库","colspan":6},  
	         {"title":"出库","colspan":5},
	         {"title":" ","colspan":1} 
	        ],
	        [    
	        {field: 'TDATE',title:'日期',sortable:true,width:100,"rowspan":1 },    
	        {field: 'BILL_NUM',title:'提单号',sortable:true,width:100,"rowspan":1},
	        {field: 'CIQ_CODE',title:'入境报检号',sortable:true,width:100,"rowspan":1},
	        {field: 'CARGO_NAME',title:'产品名称',sortable:true,width:100,"rowspan":1},
	        {field: 'ISUM_NET_WEIGHT', title:'重量（千克）', sortable:true, width:100,"rowspan":1},
	        {field: 'TRADE_TYPE',title:'贸易方式', sortable:true,width:100,"rowspan":1},
	        {field: 'LAB', title:'库位', sortable:true, width:100,"rowspan":1},
	        {field: 'STOCK_TIME', title:'出库日期', sortable:true, width:100,"rowspan":1},
	        {field: 'CERTIFICATE_TIME', title:'出具检验检疫证明日期', sortable:true, width:100,"rowspan":1},
	        {field: 'PIECE', title:'数量', sortable:true, width:100,"rowspan":1},
	        {field: 'STOCK_NAME', title:'流向', sortable:true, width:100,"rowspan":1},
	        {field: 'NUM', title:'库存', sortable:true, width:100,"rowspan":1}
	       
	        
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
		 $("#searchFrom").attr("action","${ctx}/report/stock/fisherexcel");
		 $("#searchFrom").submit();
	}
}

</script>
</body>
</html>