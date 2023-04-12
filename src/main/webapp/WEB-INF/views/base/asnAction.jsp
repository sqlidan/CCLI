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
     	        <select  id="selcur" name="filter_EQS_currencyType" class="easyui-combobox" data-options="width:150,prompt: '币种'"/>
				</select>
				<input name="filter_LIKES_asn" class="easyui-validatebox" data-options="width:150,prompt: 'asn'"/>
				<input name="filter_LIKES_billCode" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>  
				<input name="filter_LIKES_sku" class="easyui-validatebox" data-options="width:150,prompt: 'sku'"/> 
				<input name="filter_LIKES_cargoName" class="easyui-validatebox" data-options="width:150,prompt: '产品名称'"/> 
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
        </div> 
  </div>
<table id="dg"></table> 
<script type="text/javascript">
var dg;
var d;
$(document).keypress(function(e) {  
    // 回车键事件  
       if(e.which == 13) {  
   		cx(); 
       }  
   }); 
$(function(){   
	dg=$('#dg').datagrid({    
	method: "get",
    url:'${ctx}/bis/asn/asnaction', 
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
    columns:[[    
    	{field:'asn',title:'ASN' }, 
    	{field:'sku',title:'sku'},  
    	{field:'cargoName',title:'品名'}, 
    	{field:'num',title:'数量'},  
    	{field:'clientId',title:'客户名称', 
    		formatter : function(value, row, index) {
    		var a ;
				$.ajax({
					async:false,
					type: 'get',
					url: "${ctx}/base/client/getname/" + value,
					success: function(data){
						a = data;
					}
		  		});
		  		return a;
        	 }
    	},  
    	{field:'netWeight',title:'净重'},  
    	{field:'grossWeight',title:'毛重' },  
    	{field:'chargeStaDate',title:'计费开始日期' },  
    	{field:'chargeEndDate',title:'计费结束日期', },  
    	{field:'planEndDate',title:'计费计划结束日期', },  
    	{field:'lastSettlDate',title:'上次结算日期', },  
    	{field:'clientDay',title:'客户账单日', },  
    	{field:'status',title:'状态', 
    		formatter : function(value, row, index) {
        	     if(value == 1){
        	      return '正常';
        	     }
       			 if(value == 0){
        	      return '取消';
        	     }
        	 }
    	},  
    	{field:'feePlanId',title:'方案名称', 
    		formatter : function(value, row, index) {
    		if(value != null){
    		var b ;
				$.ajax({
					async:false,
					type: 'get',
					url: "${ctx}/base/scheme/getname/" + value,
					success: function(data){
						b = data;
					}
		  		});
		  		return b;
		  	 }
        	 }
    	},  
    	{field:'enterId',title:'入库联系单ID' },  
    	{field:'outId',title:'出库联系单ID' },  
    	{field:'transferId',title:'货转联系单ID' },  
    	{field:'linkTransferId',title:'关联货转单ID' },  
    	{field:'outLinkId',title:'出库订单号'},  
    	{field:'cleanSign',title:'清库标记', 
    		formatter : function(value, row, index) {
        	     if(value == 0){
        	      return '正常';
        	     }
       			 if(value == 1){
        	      return '清库';
        	     }
        	 }
    	},  
    	{field:'chargeDay',title:'计费天数'}
    ]],
    headerContextMenu: [
        {text: "冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", true).contains(field); },
         handler: function (e, field) { dg.datagrid("freezeColumn", field); }
        },
        {text: "取消冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", false).contains(field); },
         handler: function (e, field) { dg.datagrid("unfreezeColumn", field); }
        }
    ],
    enableHeaderClickMenu: true,
    enableHeaderContextMenu: true,
    enableRowContextMenu: false,
    toolbar:'#tb'
	});
});


//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

</script>
</body>
</html>