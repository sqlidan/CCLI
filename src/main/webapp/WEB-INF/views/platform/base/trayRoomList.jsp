<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
       	        <input type="text" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150,prompt: 'SKU'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		        
			</form>

        </div> 
        
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;

//document.onkeydown = function () {if(event.keyCode == 13){cx();}}

$(document).ready(function(){
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/platform/base/trayroom/json',
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'skuId',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'roomNum',title:'虚拟库',sortable:false,width:50},
//	        {field:'producingArea',title:'SKU描述',sortable:false,width:40},
			{field:'sort',title:'排序',sortable:false,width:50}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});

});

</script>
</body>
</html>