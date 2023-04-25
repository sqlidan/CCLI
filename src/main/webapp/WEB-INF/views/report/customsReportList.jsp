<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
       	        <input id="starTime" name="starTime" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt:'开始年月'"    />
       	         <input id="endTime" name="endTime" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt:'结束年月'"    />
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出EXCEL</a>
        		<span class="toolbar-item dialog-tool-separator"></span>
			</form>
        </div> 
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var nhh=0;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	dg = $('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/report/customs/customsJson', 
	    fit : true,
		fitColumns : true,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
		frozenColumns: [[    
	        {field: 'ITEM_NUM',title:'项号',sortable:true,width:100,align:'center' },    
	        {field: 'CARGO_NAME',title:'品名',sortable:true,width:100,align:'center'}
	    ]],
	    columns:[  
	        [{"title":"入          库","colspan":4},
	         {"title":"出          库","colspan":3},
	         {"title":"库          存","colspan":3},
	         {"title":"库存货值","colspan":1}],
	        [{field: 'DECLARE_TIME',title:'日期',sortable:true,width:100},
	        {field: 'CD_NUM',title:'报关单号',sortable:true,width:100},
	        {field: 'BILL_NUM', title:'提单号', sortable:true, width:100},
	        {field: 'NET_WEIGHT',title:'净重（KG）', sortable:true,width:100},
	        {field: 'OUTDECLARETIME', title:'日期', sortable:true, width:100},
	        {field: 'OUTCDNUM', title:'报关单号', sortable:true, width:100},
	        {field: 'OUTNETWEIGHT', title:'净重（KG）', sortable:true, width:100},
	        {field: 'UNIT_PRICE', title:'单价', sortable:true, width:100},
	        {field: 'WEIGHT', title:'净重（KG）', sortable:true, width:100},
	        {field: 'NUM', title:'件数', sortable:true, width:100},
	        {field: 'PRICES', title:'货值美元', sortable:true, width:180,align:'center'} 
	    ]],onLoadSuccess:function(){
	    	/*$(".txtleft").parent().parent().css("text-align","left");sss 
	    	var getDate = $("#dg").datagrid('getData');
			$("#dg").datagrid('mergeCells', {index:0,field:'Money',rowspan:getDate.,colspan:1});
 			if(nhh>0){
				 
			 }*/
		},
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});


//创建查询对象并查询
function cx(){
	var isValid = $("#searchFrom").form('validate');
	if(isValid){
		var obj=$("#searchFrom").serializeObject();
		dg.datagrid('load',obj); 	
	}
}
//导出excle
function exportExcel(){
	var isValid = $("#searchFrom").form('validate');
	if(isValid){
		 $("#searchFrom").attr("action","${ctx}/report/customs/customsexcel");
		 $("#searchFrom").submit();
	}
}

</script>
</body>
</html>