<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div  style="padding:5px; height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_itemNum" class="easyui-validatebox" data-options="width:150,prompt: '项号'"/>
      	    <input type="text" name="filter_LIKES_code" class="easyui-validatebox" data-options="width:150,prompt: '编码'"/>
      	    <input type="text" name="filter_LIKES_cargoName" class="easyui-validatebox" data-options="width:150,prompt: '商品名称'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
</div>
 <table id="hsdg"></table>
<script type="text/javascript">
 
$(document).ready(function(){
	  $('#hsdg').datagrid({    
		method: "get",
	    url:'${ctx}/base/hscode/listjson', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'skuId',
		striped:true,
		singleSelect:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 30,
		pageList : [30,60,90],
		singleSelect:false,
	    columns:[[    
	        {field:'id',title:'id',sortable:true,width:100},    
	        {field:'itemNum',title:'项号',sortable:true,width:70},
	        {field:'code',title:'HS编码',sortable:true,width:50},
	        {field:'cargoName',title:'品名',sortable:true,width:50},
	        {field:'spec',title:'规格',sortable:true,width:50} 
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false
	});
 
});
 
 
 //创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	$('#hsdg').datagrid('load',obj); 
}
</script>
</body>
</html>