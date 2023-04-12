<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="保税账册查询" style="overflow-y:auto">
<div   style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="${ctx}/supervision/taxEmsQuery/json" method="get">
      	    <input type="text" id="BwlNo" name="BwlNo" class="easyui-validatebox" data-options="width:150,prompt: '账册号'"/>
      	    <input type="text" id="TradeCode" name="TradeCode" class="easyui-validatebox" data-options="width:150,prompt: '企业代码'"/>
      	    <input type="text" name="" class="easyui-validatebox" data-options="width:150,prompt: '账册项号'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
        </div>
	<table id="dg"></table> 
</div>


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
		    url:'${ctx}/supervision/taxEmsQuery/json', 
		    	});
*/
	//$("#searchFrom").submit(); 
	dg.datagrid('load',obj); 

}
//保税账册查询列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/taxEmsQuery/json', 
	    fit : true,
		fitColumns : false,
		border : false,
		sortOrder:'desc',
		striped:true,
		//pagination:true,
		rownumbers:true,
		//pageNumber:1,
		//pageSize : 20,
		//pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field:'EmsNo',title:'统一编号',sortable:true,width:100},  
	        {field:'GdsSeqno',title:'商品序号',sortable:true,width:100},    
 	        {field:'PreQty',title:'报关库存',sortable:true,width:100},
 	        {field:'ActQty',title:'实际库存',sortable:true,width:100},
 	        {field:'TbFlag',title:'同步标记',sortable:true,width:50},
 	        {field:'DefendFlag',title:'维护库存标记',sortable:true,width:50},
 	        {field:'InDate',title:'时间',sortable:true},
 	        {field:'GdsMtno',title:'商品料号',sortable:true,width:100},
 	        {field:'Gdecd',title:'商品编码',sortable:true,width:100},
 	        {field:'GdsNm',title:'商品名称',sortable:true,width:200},
 	        {field:'GdsSpcfModelDesc',title:'商品规格型号',sortable:true,width:200},
 	        {field:'Natcd',title:'国别',sortable:true,width:50},
 	        {field:'DclUnitcd',title:'申报计量单位',sortable:true,width:200},
 	        {field:'LawfUnitcd',title:'法定计量单位',sortable:true,width:200},
 	        {field:'SecdLawfUnitcd',title:'第二法定计量单位',sortable:true,width:200},
 	        {field:'DclUprcAmt',title:'申报单价金额',sortable:true,width:200},
 	        {field:'DclCurrcd',title:'申报币制',sortable:true,width:200},
 	        {field:'AvgPrice',title:'价格',sortable:true,width:200},
 	        {field:'TotalAmt',title:'数量',sortable:true,width:200},
 	        {field:'InQty',title:'InQty',sortable:true,width:200},
 	        {field:'InLawfQty',title:'InLawfQty',sortable:true,width:200},
 	        {field:'InSecdLawfQty',title:'InSecdLawfQty',sortable:true,width:200},
 	        {field:'ActlIncQty',title:'ActlIncQty',sortable:true,width:200},
 	        {field:'ActlRedcQty',title:'ActlRedcQty',sortable:true,width:200},
 	        {field:'PrevdIncQty',title:'PrevdIncQty',sortable:true,width:200},
 	        {field:'PrevdRedcQty',title:'PrevdRedcQty',sortable:true,width:200},
 	        {field:'OutDate',title:'OutDate',sortable:true,width:200},
 	        {field:'LimitDate',title:'LimitDate',sortable:true,width:200},
 	        {field:'InvtGNo',title:'InvtGNo',sortable:true,width:200},
 	        {field:'CusmExeMarkcd',title:'CusmExeMarkcd',sortable:true,width:200},
 	        {field:'InType',title:'InType',sortable:true,width:200},
 	        {field:'InvtNo',title:'InvtNo',sortable:true,width:200},
 	        {field:'Rmk',title:'Rmk',sortable:true,width:200}
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