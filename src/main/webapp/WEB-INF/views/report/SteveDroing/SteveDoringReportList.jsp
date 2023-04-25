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
        	<input type="hidden"  id="reporttype" name="reporttype"></input>
        	<select id="reportm" name="reportm" class="easyui-combobox" data-options="width: 150,prompt:'类型'"   >
	        	<option value="" selected="true" >全部</option> 
	        	<option value="1" >入库</option> 
	        	<option value="2" >出库</option> 
	        	<option value="3" >倒箱</option> 
	        	<option value="4" >日工</option>
	        	<option value="5" >在库分拣</option>
	        	<option value="7" >缠膜</option>
	        	<option value="8" >打包</option>
	        	<option value="21" >内标签</option>
	        	<option value="22" >外标签</option>
	        	<option value="23" >码托</option>
	        	<option value="24" >装铁架</option>
	        	<option value="25" >拆铁架</option>
			</select>
			<input type="hidden"  id="clientname" name="clientname"></input>
        	   <select id="clientId" name="clientId" class="easyui-combobox" data-options="width: 150,prompt:'装卸队'" value="" > 
			</select>
		     <input type="text" name="startTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'"/>
	      - <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		    <span class="toolbar-item dialog-tool-separator"></span> 
		    <shiro:hasPermission name="report:stevetoexecl:export">
        		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出Execl</a>
        		<span class="toolbar-item dialog-tool-separator"></span>
        		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportSumExcel()">导出汇总Execl</a>
        	</shiro:hasPermission> 
			</form>  
        </div> 
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

//装卸队
$('#clientId').combobox({
	method:"GET",
	url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=2",
	valueField: 'ids',
	textField: 'clientName',
	mode:'remote' 
}); 
$("#reportm").bind("change",function() {
	$("#reporttype").val(this.combobox("getValue"));
});

 
$(function(){   
	dg = $('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/report/stevedoring/Json', 
	    fit : true,
		fitColumns : false,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field: 'LX',title:'统计类别',sortable:true,width:100},    
	        {field: 'DROSTOCKINNAME',title:'应收客户',sortable:true,width:100},
	        {field: 'CLIENT',title:'供应商',sortable:true,width:100},
	        {field: 'ASN',title:'联系单号',sortable:true,width:100},
	        {field: 'BILL_NUM',title:'提单号',sortable:true,width:100},
	        {field: 'LOADING_NUM',title:'装车号',sortable:true,width:100},    
	        {field: 'DROMR', title:'MR', sortable:true, width:100},
	        {field: 'DROENTERSTOCKTIME', title:'日期', sortable:true, width:100},
	        {field: 'FEE_PLAN',title:'品类', sortable:true,width:100},
	        {field: 'FEE_NAME', title:'费用项目', sortable:true, width:100},
	        {field: 'PRICE', title:'单价', sortable:true, width:100},
	        {field: 'NUM', title:'重量', sortable:true, width:100},
	        {field: 'SHOULD_RMB', title:'金额', sortable:true, width:100}	        
	    ]],
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
		$("#reporttype").val($("#reportm").combobox("getValue"));
		var obj=$("#searchFrom").serializeObject();
		dg.datagrid('load',obj); 	
	}
}
//导出excle
function exportExcel(){ 
	var isValid = $("#searchFrom").form('validate');
	if(isValid){ 
		 $("#searchFrom").attr("action","${ctx}/report/stevedoring/toexcel");
		 $("#searchFrom").submit();
	}
}
//导出汇总excel
function exportSumExcel(){
	var isValid = $("#searchFrom").form('validate');
	if(isValid){ 
		 $("#searchFrom").attr("action","${ctx}/report/stevedoring/tosumexcel");
		 $("#searchFrom").submit();
	}
}

</script>
</body>
</html>