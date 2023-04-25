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
      	    <input type="text" id="searchItemNum" name="searchItemNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <input type="text" name="searchCunNum" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
      	    <select id="searchStock" name="searchStockIn" class="easyui-validatebox" data-options="width:150,prompt: '存货方'" >
      	    </select>
      	   
      	    <!-- <input type="text" name="searchLinkId" class="easyui-validatebox" data-options="width:150,prompt: '入库联系单号'"/> -->
	        <input type="text" name="searchStrTime" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '入库日期起'"/>
	        <input type="text" name="searchEndTime" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '入库日期止'"/>
	        <select id="ifBonded" name="ifBonded" class="easyui-combobox" data-options="width:80,prompt: '是否保税'">
                <option value="">是否保税</option>
                <option value="1">是</option>
                <option value="0">否</option>
            </select>
	        <select id="locationType" name="locationType" class="easyui-combobox" data-options="width:150,prompt: '是否存在库位', required:'required'" >
	        	<option value="1">有库位</option>
	        	<option value="2">无库位</option>
      	    </select>
      	    <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		 
        <shiro:hasPermission name="report:stock:common">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(1)">普通客户</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="report:stock:japan">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(2)">日本客户</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="report:stock:ote">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(3)">OTE</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
         <shiro:hasPermission name="report:stock:commonp">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(1)">普通客户-PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="report:stock:japanp">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(2)">日本客户-PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="report:stock:otep">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(3)">OTE-PDF</a>
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
           url: '${ctx}/report/stock/jsonReport',//
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
               {field: 'clientName', title: '客户名称', sortable: true, width: 200},
               {field: 'clientId', title: '存货方ID', sortable: true, width: 70},
               {field: 'isBonded', title: '是否保税', sortable: true, width: 70,
                	formatter:function(val,rowData,rowIndex){
                        if(val=="1")
                            return "是";
                        else
                            return "否";
                   }
               },
               {field: 'hsCode', title: 'HS编码', sortable: true, width: 130},
               {field: 'accountBook', title: '账册商品序号', sortable: true, width: 130},
               {field: 'hsItemname', title: '海关品名', sortable: true, width: 130},
               {field: 'billCode', title: '提单号', sortable: true, width: 150},
               {field: 'bgdh', title: '报关单号', sortable: true, width: 150},
               {field: 'ycg', title: '原产国', sortable: true, width: 60},
               {field: 'bgdhdate', title: '报关审报时间', sortable: true, width: 100},
               {field: 'ctnNum', title: '箱号', sortable: true, width: 150},
               {field: 'cz', title: '箱型尺寸', sortable: true, width: 60},
               {field: 'bigName', title: '大类', sortable: true, width: 60},
               {field: 'simName', title: '小类', sortable: true, width: 60},
               {field: 'sku', title: 'SKU', sortable: true, width: 200},
               {field: 'cargoName', title: '货品名称', sortable: true, width: 150},
               {field: 'rkTime', title: '入库日期', sortable: true, width: 100},
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
               {field: 'locationCode', title: '区位', sortable: true, width: 100},
               {field: 'contactCode', title: '联系单号', sortable: true, width: 100},
               {field: 'createUser', title: '所属客服', sortable: true, width: 100}
           ]],
           enableHeaderClickMenu: true,
           enableHeaderContextMenu: true,
           enableRowContextMenu: false,
           toolbar:'#tb'
       });
});	 

function report(ntype){
	if ($("#searchFrom").form('validate')) {
		$("#ntype").val(ntype);
		var url = "${ctx}/report/stock/report";
		var billNum=$("#searchItemNum").val();
	 	$("#searchFrom").attr("action",url).submit();
	}
}
function reportpdf(ntype){
	if ($("#searchFrom").form('validate')) {
		$("#ntype").val(ntype);
		var url = "${ctx}/report/stock/reportpdf";
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