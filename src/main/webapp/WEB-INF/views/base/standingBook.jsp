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
     	        <select  id="customsName" name="filter_EQS_customsName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'"/>
				</select>
		        <span class="toolbar-item dialog-tool-separator"></span>
		         <input  id="asn" name="filter_LIKES_asn" class="easyui-validatebox" data-options="width:150,prompt: 'asn'"/>
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
	//客户
	   $('#customsName').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	   });

	dg=$('#dg').datagrid({    
	method: "get",
    url:'${ctx}/cost/standingBook/json', 
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
//    	{field:'standingNum',title:'台账编号' }, 
    	{field:'standingCode',title:'台账编码' }, 
    	{field:'customsNum',title:'客户编号'},  
    	{field:'customsName',title:'客户名称'},  
    	{field:'billNum',title:'提单号' },  
    	{field:'sku',title:'sku' },
    	{field:'linkId',title:'联系单号' },  
    	{field:'crkSign',title:'入库标志',
    		formatter : function(value, row, index) {
        	     if(value == 1){
        	      return '入库';
        	     }
       			 if(value == 2){
        	      return '出库';
        	     }
        	      if(value == 3){
        	      return '货转';
        	     }
        	 }
    	},  
    	{field:'storageDtae',title:'入库时间', },  
    	{field:'feeCode',title:'费目代码', },  
    	{field:'feeName',title:'费目名称', },  
    	{field:'feePlan',title:'费用方案ID', },  
    	{field:'asn',title:'ASN'},  
    	{field:'ifReceive',title:'应收应付', 
    		formatter : function(value, row, index) {
        	     if(value == 1){
        	      return '应收';
        	     }
       			 if(value == 2){
        	      return '应付';
        	     }
        	 }
    	},  
    	{field:'num',title:'数量' },  
    	{field:'price',title:'单价' },  
    	{field:'receiveAmount',title:'应收金额 : 单价金额*数量 ' },  
    	{field:'realAmount',title:'实收金额' },  
    	{field:'chargeDate',title:'计费时间'},  
    	{field:'costDate',title:'费用产生事件'},  
    	{field:'fillSign',title:'充补标志', 
    		formatter : function(value, row, index) {
        	     if(value == 0){
        	      return '未补充';
        	     }
       			 if(value == 1){
        	      return '已补充';
        	     }
        	 }
    	},  
    	{field:'farePerson',title:'补票人'},  
    	{field:'fareDate',title:'补票时间'},  
    	{field:'currency',title:'币种',
    		formatter : function(value, row, index) {
	 	        	if(value == '0'){
	 	        		return "人民币";
	 	        	}
	 	        	if(value == '1'){
	 	        		return "美元";
	 	        	}
	 	        	if(value == '2'){
	 	        		return "日元";
	 	        	}
	 	        	if(value == '201'){
	 	        		return "阿尔及利亚第纳尔";
	 	        	}
 	        	}
 	    },  
    	{field:'inputPerson',title:'录入员'},  
    	{field:'inputDate',title:'录入时间'},  
    	{field:'collectInvNum',title:'代收发票号'},  
    	{field:'salesman',title:'业务员'},  
    	{field:'exchangeRate',title:'汇率'},  
    	{field:'examineSign',title:'审核标志', 
    		formatter : function(value, row, index) {
        	     if(value == 0){
        	      return '未审核';
        	     }
       			 if(value == 1){
        	      return '已审核';
        	     }
        	 }
    	},  
    	{field:'examinePerson',title:'审核人' },  
    	{field:'examineDate',title:'审核时间'},  
    	{field:'bisType',title:'业务类型',
    		formatter : function(value, row, index) {
        	     if(value == 1){
        	      return '其他';
        	     }
       			 if(value == 2){
        	      return '仓储费';
        	     }
        	      if(value == 3){
        	      return '出入库费 ';
        	     }
        	     if(value == 4){
        	      return '分拣费';
        	     }
        	     if(value == 5){
        	      return '应付分拣费';
        	     }
        	     if(value == 6){
        	      return '人工装卸费';
        	     }
        	     if(value == 7){
        	      return '缠膜费';
        	     }
        	     if(value == 8){
        	      return '打包费';
        	     }
        	 }
    	 },  
    	{field:'shouldRmb',title:'应收金额RMB'},  
    	{field:'realRmb',title:'实收金额RMB'},  
    	{field:'org',title:'公司 （代码）' },  
    	{field:'department',title:'部门（代码）'},  
    	{field:'reconcileNum',title:'对帐编号'},  
    	{field:'reconcileSign',title:'对帐标志',
    	formatter : function(value, row, index) { 
    		if(value == 0){
        	  return '未对账';
        	}
       		if(value == 1){
        	  return '已对账';
        	}
        }
    	},   
        {field:'reconcilePerson',title:'对帐人',sortable:true},    
        {field:'reconcileDate',title:'对帐时间',sortable:true}, 
        {field:'settleSign',title:'结算标志 ', 
        formatter : function(value, row, index) {
    		if(value == 0){
        	   return '未结算';
        	}
       		if(value == 1){
        	   return '已结算';
        	}
        }
    	},   
        {field:'settlePerson',title:'结算人',sortable:true,width:100},
        {field:'settleDate',title:'结算时间',sortable:true,width:100},
        {field:'taxRate',title:'税率',sortable:true,width:100},
        {field:'invoiceCode',title:'发票(代码)',sortable:true,width:100},
        {field:'invoiceNum',title:'发票号',sortable:true,width:100},
        {field:'splitSign',title:'分割标志',sortable:true,width:100, 
        formatter : function(value, row, index) {
    		if(value == 0){
        	   return '未分割';
        	}
       		if(value == 1){
        	   return '已分割';
        	}
        }
        },
        {field:'remark',title:'备注',sortable:true,width:100},
        {field:'contactType',title:'联系单类型',sortable:true,width:100, 
        formatter : function(value, row, index) {
    		if(value == 1){
        	  return '入库';
        	}
       		if(value == 2){
        	  return '出库';
        	}
        	if(value == 3){
        	  return '货转';
        	 }
         }
         },
         {field:'boxSign',title:'倒箱标记',sortable:true,width:100,
          formatter : function(value, row, index) {
    		if(value == 1){
        	  return '倒箱 ';
        	}
       		if(value == 0){
        	  return '其他';
        	}
         }
         },
         {field:'shareSign',title:'分摊标记',sortable:true,width:100,
         formatter : function(value, row, index) { 
    		if(value == 1){
        	  return '分摊 ';
        	}
       		if(value == 0){
        	  return '不分摊';
        	}
         }
         },
         {field:'paySign',title:'垫付标记',sortable:true,width:100, 
         formatter : function(value, row, index) {
    		if(value == 1){
        	  return '已垫付 ';
        	}
       		if(value == 0){
        	  return '未垫付';
        	}
         }
         }, 
          {field:'chargeSign',title:'收款标记',sortable:true,width:100, 
           formatter : function(value, row, index) {
    		if(value == 1){
        	  return '已收';
        	}
       		if(value == 0){
        	  return '未收';
        	}
         }
         }, 
        {field:'chargePerson',title:'收款人',sortable:true,width:100},
        {field:'chargeTime',title:'收款时间',sortable:true,width:100},
        {field:'billDate',title:'账单年月',sortable:true,width:100}
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