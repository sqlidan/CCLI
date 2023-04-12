<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<!--  
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
			</form>
        </div> 
</div>
-->
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
$(document).ready(function(){
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/cost/cancelafter/jsonhxinfo', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : '',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[
	        {field:'CUSTOMS_NUM',title:'CUSTOMS_NUM',hidden:true},    
	        {field:'CUSTOMS_NAME',title:'客户名称',sortable:true,width:150},    
	        {field:'BILL_DATE',title:'账单年月',sortable:true,width:100,
	        	formatter : function(value, row, index) {
	        		if(value!=null && value!="" && value.indexOf(":")>0){
	        			value=value.substring(0,value.lastIndexOf("-"));	
	        		}
	        		if(value!=null && value!="" && value.split("-").length==3){
	        			value=value.substring(0,value.lastIndexOf("-"));	
	        		}
	       			return value;
	        	}},
        	{field:'LINK_ID',title:'业务单号',sortable:true,width:120},
        	{field:'BILL_NUM',title:'提单号',sortable:true,width:120},
        	{field:'FEE_NAME',title:'费目',sortable:true,width:100},
        	{field:'PRICE',title:'单价',sortable:true,width:100},
	        {field:'YSMONEY',title:'应收RMB',sortable:true,width:100},
	        {field:'NOWMONEY',title:'本次实收RMB',sortable:true,width:100}
	         
	    ]],queryParams: {
	    	num:'${num}'
		},
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
	 
});
</script>
</body>
</html>