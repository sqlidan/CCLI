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
      	    <input type="text" id="searchTransferNum" name="searchTransferNum"  class="easyui-validatebox" data-options="width:150,prompt: '货转单号（多个货转单号请用英文的逗号分割）'"/>
      	    <select id="ifBonded" name="ifBonded" class="easyui-combobox"
                    data-options="width:150,prompt: '是否保税'">
                <option value="">是否保税</option>
                <option value="1">是</option>
                <option value="0">否</option>
            </select>
      	    <select id="churu" name="churu"  class="easyui-combobox" data-options="width:150" >
      	    	<option value='1'>入库</option>
      	    	<option value='2'>出库</option>
      	    </select>
      	    <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		 
        <shiro:hasPermission name="wms:transfer:common">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(1)">普通客户</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:transfer:japan">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(2)">日本客户</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:transfer:ote">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(3)">OTE</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
     <shiro:hasPermission name="wms:transfer:commonp">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(1)">普通客户-PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:transfer:japanp">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(2)">日本客户-PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:transfer:otep">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(3)">OTE-PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
$(function(){   
	//客户
	   $('#searchStock').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote'
   	});
	
		 //加载页面数据表格
       dg = $('#dg').datagrid({
           method: "get",
           url: '${ctx}/bis/transfer/jsonReport',//
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
			   {field: 'contactCode', title: '货转单号', sortable: true, width: 200},       
               {field: 'clientName', title: '存货方名称', sortable: true, width: 200},
               {field: 'isBonded', title: '是否保税', sortable: true, width: 70,
                 	formatter:function(val,rowData,rowIndex){
                         if(val=="1")
                             return "是";
                         else
                             return "否";
                    }
                },
               {field: 'billCode', title: '提单号', sortable: true, width: 150},
               {field: 'ctnNum', title: '箱号', sortable: true, width: 150},
               {field: 'sku', title: 'SKU', sortable: true, width: 200},
               {field: 'cargoName', title: '货品名称', sortable: true, width: 150},
               {field: 'enterTime', title: '货转日期', sortable: true, width: 100},
               {field: 'warehouse', title: '收货方名称', sortable: true, width: 100},
               {field: 'state', title: '货物状态', sortable: true, width: 100},
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
               {field: 'inTime', title: '操作日期', sortable: true, width: 100}
               
           ]],
           enableHeaderClickMenu: true,
           enableHeaderContextMenu: true,
           enableRowContextMenu: false,
           toolbar:'#tb'
       });
   	
});	 

function report(ntype){
	if($("#searchFrom").form('validate')){
		$("#ntype").val(ntype);
		var url = "${ctx}/bis/transfer/report";
	 	$("#searchFrom").attr("action",url).submit();
 	}
}
function reportpdf(ntype){
	if($("#searchFrom").form('validate')){
		$("#ntype").val(ntype);
		var url = "${ctx}/bis/transfer/reportpdf";
	 	$("#searchFrom").attr("action",url).submit();
 	}
}

//创建查询对象并查询
function cx() {
    var obj = $("#searchFrom").serializeObject();
    dg.datagrid('load', obj);
}
</script>
</body>
</html>