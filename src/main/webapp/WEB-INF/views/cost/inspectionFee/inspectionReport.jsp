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
      	    <select id="searchStock" name="clientId" class="easyui-combobox" data-options="width:150,prompt: '客户'" >
      	    </select>
	        <input type="text" name="startTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '查验时间'"/>
	     -  <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '查验时间'"/>
	        <select type="text" name="ifLx" class="easyui-combobox" data-options="width:150,prompt: '是否零星'" >
	        	<option value=""></option>
      	    	<option value='1'>是</option>
      	    	<option value='0'>否</option>
      	     </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportIt()">导出</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
	</div> 
</div>

<table id="dg"></table> 

<script type="text/javascript">
var dg;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	//客户
	   $('#searchStock').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
   	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/cost/inspecion/reportjson', 
	    fit : true,
		fitColumns : false,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 1000,
		pageList : [ 1000, 2000, 3000 ],
		singleSelect:true,
	    columns:[[   
	        {field:'CLIENT_NAME',title:'客户名称',sortable:true},    
 	        {field:'SHUICHANCY',title:'水产平台服务',sortable:true,width:150},
 	        {field:'DONGROUCY',title:'冻肉平台服务',sortable:true,width:150},
 	        {field:'SHUIGUOCY',title:'水果平台服务',sortable:true,width:150},
	        {field:'QITACY',title:'其他平台服务',sortable:true,width:150},
 	        {field:'CHADIAN',title:'插电',sortable:true,width:200},
 	        {field:'DIAOXIANG',title:'吊箱',sortable:true,width:100},
 	        {field:'CHANGDI',title:'场地',sortable:true,width:200},
 	        {field:'BANDAO',title:'搬倒',sortable:true,width:100},
 	        {field:'YEAR',title:'年份',sortable:true,width:100},
  	        {field:'MONTH',title:'月份',sortable:true},
  	        {field:'COST',title:'合计',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//报表导出
function exportIt(){
	var url = "/ccli/cost/inspecion/export";
	$("#searchFrom").attr("action",url).submit();
}



//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}



</script>
</body>
</html>