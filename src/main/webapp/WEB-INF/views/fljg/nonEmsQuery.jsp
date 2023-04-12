<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<%--<div data-options="region:'center'" title="非保税账册查询" style="overflow-y:auto">--%>
<div id="tb" style="padding:5px;height:auto">
<div>
		<form id="searchFrom" action="">
      	    <input type="text" name="EmsNo" class="easyui-validatebox" data-options="width:150,prompt: '账册号'"/>
      	    <input type="text" name="CodeTs" class="easyui-validatebox" data-options="width:150,prompt: '商品编码'"/>
      	    <input type="text" name="GNo" class="easyui-validatebox" data-options="width:150,prompt: '账册项号'"/>
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
var dgg;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	gridDG();
});
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
/*	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/nonEmsQuery/json', 
	});*/
	dg.datagrid('load',obj); 
}
//非保税账册查询列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/nonEmsQuery/json',
	    fit : true,
		fitColumns : true,
		border : false,
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field:'EmsNo',title:'账册编号',sortable:true,width:250},  
	        {field:'GType',title:'GType',sortable:true,width:150},    
 	        {field:'GNo',title:'序号',sortable:true,width:100},
 	        {field:'CopGNo',title:'企业货物编号',sortable:true,width:100},
 	        {field:'CodeTs',title:'商品编码',sortable:true,width:200},
 	        {field:'GName',title:'商品名称',sortable:true,width:200},
 	        {field:'GModel',title:'商品规格型号',sortable:true,width:120},
 	        {field:'Unit',title:'申报计量单位',sortable:true,width:150},
 	        {field:'Unit1',title:'法定计量单位',sortable:true,width:150},
 	        {field:'Unit2',title:'法定第二单位',sortable:true,width:250},
 	        {field:'ApprQty',title:'申请数量',sortable:true,width:180},
 	        {field:'CutQty',title:'剩余数量',sortable:true,width:180},
 	        {field:'DeductQty',title:'过闸数量',sortable:true,width:180},
 	        {field:'ConfirmQty',title:'到货数量',sortable:true,width:180},
 	        {field:'SrcBillId',title:'来源单号',sortable:true,width:350},
 	        {field:'SrcGNo',title:'来源商品序号',sortable:true,width:200},
 	        {field:'Note',title:'备注',sortable:true,width:200},
 	        {field:'Qty1',title:'法定数量',sortable:true,width:180},
 	        {field:'GdsMtno',title:'商品料号',sortable:true,width:200}
	    ]],
	    onClickRow:function(rowIndex, rowData){
	    	info(rowData.linkId);
	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}



</script>
</body>
</html>