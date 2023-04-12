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
     	    <select id="clientNameS" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'" >
     	    </select>
      	    <input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <input type="text" name="filter_LIKES_ctnNum" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
      	    <input type="text" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150,prompt: 'SKU'"/>
      	    <select id="warehouseId" name="filter_LIKES_warehouseId" class="easyui-combobox" data-options="width:150,prompt: '仓库'" >
     	    </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="wms:pledge:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('质押管理','wms/pledge/manager')">质押</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:pledge:cancle">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('撤押管理','wms/pledge/pledgeC')">撤押</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
         onclick="exportExcel()">质押导出</a>
         <span class="toolbar-item dialog-tool-separator"></span>
        
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var d;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	//客户
	   $('#clientNameS').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
   	
   	 //仓库
	 $.ajax({
	   type: "GET",
	   async : false,
	   url: "${ctx}/base/warehouse/getWarehouse",
	   data: "",
	   dataType: "json",
	   success: function(date){
		   for(var i=0; i<date.length; i++){
			   $('#warehouseId').combobox({
				   data: date,
				   valueField: 'id',
				   textField: 'warehouseName',
				   editable : false
			   });
		   }
	   }
	});
   	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/pledge/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'codeNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
 	        {field:'CLIENTNAME',title:'客户',sortable:true,width:100},
 	        {field:'BILLNUM',title:'提单号',sortable:true,width:100},
 	        {field:'CTNNUM',title:'箱号',sortable:true,width:100},
 	        {field:'SKU',title:'SKU',sortable:true,width:100},
 	        {field:'PNAME',title:'品名',sortable:true,width:100},
 	        {field:'NUM',title:'件数',sortable:true,width:100},
 	        {field:'NETWEIGHT',title:'总净重',sortable:true,width:100},
  	        {field:'PTYPE',title:'质押类型',sortable:true,
  	        	formatter : function(value, row, index) {
 	       			return value == "1" ? '静态质押':'动态质押';
 	        	}
  	        },
  	        {field:'WAREHOUSE',title:'仓库',sortable:true,width:100}
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

//导出
function exportExcel() {
    var url = "${ctx}/wms/pledge/export";
    $("#searchFrom").attr("action", url).submit();
}


</script>
</body>
</html>