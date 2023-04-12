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
      	    <input type="text" name="asnId" class="easyui-validatebox" data-options="width:150,required:'required',prompt: 'ASN号'"/>
	      <!--  <input type="text" name="strartTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '入库日期起'"/>
	        <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '入库日期止'"/> 
	      -->
	        <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
	       	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report()">导出EXCEL</a>
	       	<span class="toolbar-item dialog-tool-separator"></span>
	       	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf()">导出PDF</a>
	       	<span class="toolbar-item dialog-tool-separator"></span>
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
$(function(){
	
	  //加载页面数据表格
    dg = $('#dg').datagrid({
        method: "get",
        url: '${ctx}/bis/asninfo/reportJson',//
        fit: true,
        fitColumns: false,
        border: false,
        //idField: 'linkId',
        sortOrder: 'desc',
        striped: true,
        pagination: true,
        rownumbers: true,
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 40, 50],
        singleSelect: true,
        columns: [[
			{field: 'asn', title: 'ASN', sortable: true, width: 200},
            {field: 'clientName', title: '客户名称', hidden:true,sortable: true, width: 200},
            {field: 'clientId', title: '存货方ID', hidden:true,sortable: true, width: 70},
            {field: 'billCode', title: '提单号', sortable: true, width: 150},
            {field: 'ctnNum', title: '箱号', sortable: true, width: 200},
            {field: 'sku', title: 'SKU', sortable: true, width: 200},
            {field: 'cargoName', title: '货品名称', sortable: true, width: 300},
            {field: 'enterTime', title: '入库日期', hidden:true,sortable: true, width: 100},
            {field: 'state', title: '货物状态', hidden:true,sortable: true, width: 100},
            {field: 'nowNum', title: '总件数', sortable: true, width: 100},
            {field: 'allnet', title: '总净重', hidden:true,sortable: true, width: 100},
            {field: 'allgross', title: '总毛重', hidden:true,sortable: true, width: 100},
            {field: 'cargoType', title: '小类', hidden:true,sortable: true, width: 100},
            {field: 'netWeight', title: '单净重', hidden:true,sortable: true, width: 100},
            {field: 'grossWeight', title: '单毛重', hidden:true,sortable: true, width: 100},
            {field: 'trayCode', title: '托盘号', sortable: true, width: 200},
            {field: 'locationCode', title: '库位', sortable: true, width: 200}
            
        ]],
   
        enableHeaderClickMenu: true,
        enableHeaderContextMenu: true,
        enableRowContextMenu: false,
        toolbar:'#tb'
    });
});	 

//导出Excel
function report(){
	if ($("#searchFrom").form('validate')) {
		var url = "${ctx}/bis/asninfo/exportExcel";
	 	$("#searchFrom").attr("action",url).submit();
	}
}

//导出PDF
function reportpdf(){
	if ($("#searchFrom").form('validate')) {
		var url = "${ctx}/bis/asninfo/exportPDF";
	 	$("#searchFrom").attr("action",url).submit();
	}
}
</script>
</body>
</html>