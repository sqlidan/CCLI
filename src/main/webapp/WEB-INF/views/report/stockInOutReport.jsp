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
			<input type="hidden" name="page" id="page" value="1"/>
			<input type="hidden" name="rows" id="rows" value="1000"/>
			<input type="hidden" name="ntype" id="ntype"/>
      	    <input type="text" id="searchItemNum" name="searchItemNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <input type="text" name="searchCunNum" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
      	    <select id="searchStock" name="searchStockIn" class="easyui-validatebox" data-options="width:150,prompt: '存货方'" >
      	    </select>
      	    <select id="ifBonded" name="ifBonded" class="easyui-combobox" data-options="width:80,prompt: '是否保税'">
                <option value="">是否保税</option>
                <option value="1">是</option>
                <option value="0">否</option>
            </select>
	        <input type="text" name="searchStrTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"    data-options="width:150,prompt: '日期起',required:'required'"/>
	        <input type="text" name="searchEndTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"    data-options="width:150,prompt: '日期止',required:'required'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		 
        <shiro:hasPermission name="report:stock:without">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(1)">不带区位</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="report:stock:with">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report(2)">带区位</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="report:stock:withoutp">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(1)">不带区位-PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="report:stock:withp">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf(2)">带区位-PDF</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
	</div> 
</div>

<table id="dg"><tr><td colspan="1"></td></tr></table> 
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
           url: '${ctx}/report/stock/inoutJson',//
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
               {field: 'clientName', title: '存货方', sortable: true, width: 200},
               {field: 'clientId', title: '存货方ID', sortable: true, width: 70},
               {field: 'isBonded', title: '是否保税', sortable: true, width: 70,
               	formatter:function(val,rowData,rowIndex){
                       if(val=="1")
                           return "是";
                       else
                           return "否";
                  }
               },
               {field: 'reportType', title: '作业类型', sortable: true, width:60},
               {field: 'shf', title: '收货方', sortable: true, width: 100},
               {field: 'billCode', title: '提单号', sortable: true, width: 150},
               {field: 'ctnNum', title: '箱号', sortable: true, width: 150},
               {field: 'bgdh', title: '报关单号', sortable: true, width: 150},
               {field: 'ycg', title: '原产国', sortable: true, width:60},
               {field: 'bgdhdate', title: '报关申报日期', sortable: true, width:80},
               {field: 'sku', title: 'SKU', sortable: true, width: 200},
               {field: 'cz', title: '箱型尺寸', sortable: true, width: 60},
               {field: 'bigName', title: '类型名', sortable: true, width:80},
               {field: 'simName', title: '小类', sortable: true, width:80},
               {field: 'allpiece', title: '总件数', sortable: true, width: 100},
               {field: 'enterTime', title: '入库日期', sortable: true, width: 100},
               {field: 'state', title: '货物状态', sortable: true, width: 100},
               {field: 'cargoName', title: '货品名称', sortable: true, width: 150},
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
               {field: 'locationCode', title: '库位', sortable: true, width: 100},
               {field: 'contactCode', title: '联系单号', sortable: true, width: 100},
               {field: 'createUser', title: '所属客服', sortable: true, width: 100},
           ]],
           enableHeaderClickMenu: true,
           enableHeaderContextMenu: true,
           enableRowContextMenu: false,
           toolbar:'#tb'
       });
   	
});	
//excel导出
function report(ntype){
	if ($("#searchFrom").form('validate')) {
		$("#ntype").val(ntype);
		var url = "${ctx}/report/stock/inoutport";
	 	$("#searchFrom").attr("action",url).submit();
	}
}
//PDF 导出
function reportpdf(ntype){
	if ($("#searchFrom").form('validate')) {
		$("#ntype").val(ntype);
		var url = "${ctx}/report/stock/ioreportpdf";
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