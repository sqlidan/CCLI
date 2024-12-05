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
		<form id="searchFrom" action="" method="post">
			<input type="hidden" name="ntype" id="ntype"/>
      	    <input type="text" name="filter_EQS_billCode" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="createSQD()">批量生成申请单</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="createSB()">申请单批量申报</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="createXNHFD()">批量生成虚拟核放单</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="createSB2()">核放单批量申报</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="createDHQR()">批量到货确认</a>
		<span class="toolbar-item dialog-tool-separator"></span>
	</div>
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var obj = $("#searchFrom").serializeObject();
$(function(){
	//加载页面数据表格
       dg = $('#dg').datagrid({
           method: "get",
           url: '${ctx}/report/ATray/jsonReportNewTemp',
           fit: true,
           fitColumns: false,
           border: false,
           queryParams:obj,
           sortOrder: 'desc',
           striped: true,
           pagination: true,
           rownumbers: true,
           pageNumber: 1,
           pageSize: 20,
           pageList: [10, 20, 30, 40, 50],
           singleSelect: true,
           columns: [[
               {field: 'xh', title: '项号', sortable: true, width: 130},
               {field: 'billCode', title: '提单号', sortable: true, width: 150},
               {field: 'nowNum', title: '总件数', sortable: true, width: 100},
               {field: 'allnet', title: '总净重', sortable: true, width: 100,
            	   formatter:function(val,rowData,rowIndex){
                       if(val!=null)
                           return val.toFixed(2);
                  }   
               },
               {field: 'allgross', title: '总毛重', sortable: true, width: 100,
            	   formatter:function(val,rowData,rowIndex){
                       if(val!=null)
                           return val.toFixed(2);
                  }   
               },
			   {field: 'createsqd', title: '生成申请单', sortable: true, width: 100},
			   {field: 'createsb', title: '申报', sortable: true, width: 100},
			   {field: 'createxnhfd', title: '生成虚拟核放单', sortable: true, width: 100},
			   {field: 'createdhqr', title: '到货确认', sortable: true, width: 100}
           ]],
           enableHeaderClickMenu: true,
           enableHeaderContextMenu: true,
           enableRowContextMenu: false,
           toolbar:'#tb'
       });
});

function create(){
	parent.$.messager.confirm('提示', '确定重新依据在库明细整理生成库存信息？', function(data){
		if (data){
			$.ajax({
				async:false,
				type:'get',
				url:"${ctx}/report/ATray/createDataNewTemp",
				success: function(data){
					if(data == "success"){
						parent.$.easyui.messager.alert("操作成功！");
						cx();
					}else{
						parent.$.easyui.messager.alert("操作失败！");
					}
				}
			});
		}
	});
}
function createSQD(){
	parent.$.messager.confirm('提示', '确定批量执行生成申请单？', function(data){
		if (data){
			$.ajax({
				async:false,
				type:'get',
				url:"${ctx}/report/ATray/createSQD",
				success: function(data){
					if(data == "success"){
						parent.$.easyui.messager.alert("操作成功！");
						cx();
					}else{
						parent.$.easyui.messager.alert("操作失败！");
					}
				}
			});
		}
	});
}
function createSB(){
	parent.$.messager.confirm('提示', '确定批量执行申请单申报？', function(data){
		if (data){
			$.ajax({
				async:false,
				type:'get',
				url:"${ctx}/report/ATray/createSB",
				success: function(data){
					if(data == "success"){
						parent.$.easyui.messager.alert("操作成功！");
						cx();
					}else{
						parent.$.easyui.messager.alert("操作失败！");
					}
				}
			});
		}
	});
}
function createXNHFD(){
	parent.$.messager.confirm('提示', '确定批量执行生成虚拟核放单？', function(data){
		if (data){
			$.ajax({
				async:false,
				type:'get',
				url:"${ctx}/report/ATray/createXNHFD",
				success: function(data){
					if(data == "success"){
						parent.$.easyui.messager.alert("操作成功！");
						cx();
					}else{
						parent.$.easyui.messager.alert("操作失败！");
					}
				}
			});
		}
	});
}
function createSB2(){
	parent.$.messager.confirm('提示', '确定批量执行核放单申报？', function(data){
		if (data){
			$.ajax({
				async:false,
				type:'get',
				url:"${ctx}/report/ATray/createSB2",
				success: function(data){
					if(data == "success"){
						parent.$.easyui.messager.alert("操作成功！");
						cx();
					}else{
						parent.$.easyui.messager.alert("操作失败！");
					}
				}
			});
		}
	});
}
function createDHQR(){
	parent.$.messager.confirm('提示', '确定批量执行到货确认？', function(data){
		if (data){
			$.ajax({
				async:false,
				type:'get',
				url:"${ctx}/report/ATray/createDHQR",
				success: function(data){
					if(data == "success"){
						parent.$.easyui.messager.alert("操作成功！");
						cx();
					}else{
						parent.$.easyui.messager.alert("操作失败！");
					}
				}
			});
		}
	});
}

//创建查询对象并查询
function cx() {
    var obj = $("#searchFrom").serializeObject();
    dg.datagrid('load', obj);
}
</script>
</body>
</html>