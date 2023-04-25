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
      	    <input type="text" id="searchItemNum" name="searchItemNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <input type="text" name="searchCunNum" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
      	    <select id="searchStock" name="searchStockIn" class="easyui-validatebox" data-options="width:150,prompt: '客户名称'" >
      	    </select>
	        <input type="text" name="searchStrTime" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '开始日期'"/>
	        <input type="text" name="searchEndTime" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '结束日期'"/>
	        <select id="ifBonded" name="ifBonded" class="easyui-combobox" data-options="width:80,prompt: '类型'">
                <option value="">全部</option>
                <option value="1">倒箱</option>
                <option value="0">查验</option>
            </select>
      	    <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		 
        <shiro:hasPermission name="report:stock:boxcheck:excel">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report()">导出Excel</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
       
         <shiro:hasPermission name="report:stock:boxcheck:pdf">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf()">导出PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var obj = $("#searchFrom").serializeObject();
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
           url: '${ctx}/report/stock/boxCheckReport',
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
               {field: 'bgdhdate', title: '日期', sortable: true, width: 100},
               {field: 'isBonded', title: '类型', sortable: true, width: 70,
               	formatter:function(val,rowData,rowIndex){
                       if(val=="倒箱")
                           return "<font color='green'>倒箱</font>";
                       else
                           return "<font color='red'>查验</font>";
                  }
                },
               {field: 'clientName', title: '客户名称', sortable: true, width: 200},
               {field: 'clientId', title: '客户ID', sortable: true, width: 70},
               {field: 'billCode', title: '提单号', sortable: true, width: 150},
               {field: 'ctnNum', title: '箱号', sortable: true, width: 150},
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
               {field: 'bigName', title: '大类', sortable: true, width: 60},
               {field: 'simName', title: '小类', sortable: true, width: 60},
               {field: 'cargoName', title: '货品名称', sortable: true, width: 150},
               {field: 'contactCode', title: '联系单号', sortable: true, width: 100},
               {field: 'createUser', title: '所属客服', sortable: true, width: 100}
           ]],
           enableHeaderClickMenu: true,
           enableHeaderContextMenu: true,
           enableRowContextMenu: false,
           toolbar:'#tb'
       });
});	 

function report(){
	if ($("#searchFrom").form('validate')) {
		var url = "${ctx}/report/stock/boxCheckExcel";
		var billNum=$("#searchItemNum").val();
	 	$("#searchFrom").attr("action",url).submit();
	}
}
function reportpdf(){
	if ($("#searchFrom").form('validate')) {
		var url = "${ctx}/report/stock/boxCheckPdf";
		var billNum=$("#searchItemNum").val();
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