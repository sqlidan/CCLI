<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
 		<div>
		<form id="searchFrom" action="">
      	    <input type="text" id="getRk" class="easyui-validatebox" data-options="width:150,prompt: '入库号'"/>
      	    <input type="text" id="getBill" name="filter_LIKES_stockIn" class="easyui-validatebox" data-options="width:150,prompt: '提单号'" />
      	    <input type="text" id="getMr" class="easyui-validatebox" data-options="width:150,prompt: 'mr'"/>
      	    <input type="text" id="getSku" class="easyui-validatebox" data-options="width:150,prompt: 'sku'"/>
      	    <input type="text" id="getName" class="easyui-validatebox" data-options="width:150,prompt: '品名'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		</div>
 <table id="infodg"></table>
<script type="text/javascript">
 
$(document).ready(function(){
	  gridRK();
});

function cx(){
	gridRK();
}

function gridRK(){
	$('#infodg').datagrid({    
		method: "get",
	    url:'${ctx}/bis/trayinfo/ctrayjson', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'LAB',
		striped:true,
		pagination:false,
		rownumbers:true,
		pageNumber:1,
		pageSize :1000,
		pageList : [1000],
		singleSelect:false,
	    columns:[[    
	        {field:'STOCK_NAME',title:'存货方',sortable:false,width:80},    
	        {field:'BILL_NUM',title:'提单号',sortable:false,width:80},
	        {field:'CTN_NUM',title:'箱号',sortable:false,width:80 },
	        {field:'ASN',title:'ASN',sortable:false,width:80 },
	        {field:'SKU_ID',title:'SKU',sortable:false,width:140},
	        {field:'MSC_NUM',title:'MSC号',sortable:false,width:80},
	        {field:'LOT_NUM',title:'LOT号',sortable:false,width:80},
	        {field:'TYPE_SIZE',title:'规格',sortable:false,width:80},
  	        {field:'RK_NUM',title:'入库号',sortable:false,width:140},
	        {field:'CARGO_NAME',title:'品名',sortable:false,width:80},
	        {field:'ENTER_STATE',title:'入库类型',sortable:false,width:50,
	        	formatter : function(value, row, index) {
	        		if(value==0){return '成品'}else if(value==1){return '货损'}
	        	}
	        },
	        {field:'NOW_PIECE',title:'库存件数',sortable:false,width:80},
	        {field:'TNUM',title:'静态质押件数',sortable:false,width:80},
	        {field:'GNUM',title:'要出库件数',sortable:false,width:80},
	        {field:'PIECE',title:'件数',sortable:false,width:50},
	        {field:'REMARKA',title:'备注',sortable:false,width:140},
	        {field:'NET_SINGLE',title:'',hidden:true},
	        {field:'GROSS_SINGLE',title:'',hidden:true},
	        {field:'BEFORE_STOCK_IN',title:'原货主id',hidden:true},
	        {field:'LAB',title:'',hidden:true},
	        {field:'NUM',title:'',hidden:true}
	    ]],
	    queryParams: {
	    	clientId:'${order.stockIn}',
	    	ckId:'${order.warehouseId}',
	    	outCode:'${order.outLinkId}',
	    	rkNum:$("#getRk").val(),
	    	orderCode:'${order.remark}',
	    	getBill:$("#getBill").val(),
	    	getSku:$("#getSku").val(),
	    	getMr:$("#getMr").val(),
	    	getName:$("#getName").val()
		},
		onLoadSuccess:function(){
			  var rows =  $('#infodg').datagrid('getRows');
			  var inforows =  $('#dg').datagrid('getRows');
			  if (rows!=null && rows.length>0) {
	            	trNum=rows.length;
	            	$(rows).each(function(i){
	            		var row=rows[i];
	            		var getVal="";
	            		var remark="";
	            		var rk="";
	            		$.ajax({
 							async:false,
 							type: 'get',
 							url: "${ctx}/base/sku/getrkdh/" + row.SKU_ID,
 							success: function(data){
 								rk = data;
 							}
 		  				});
	            		if(inforows!=null && inforows.length>0){
	            			getVal=getInfoRowObj(inforows,row["LAB"]);
	            			remark = getRemark(inforows,row["LAB"]); 
	            		}
	            		$('#infodg').datagrid('updateRow',{
                    		index:i,
                    		row: {
                    			PIECE: '<input type=\"text\" id=\"piece'+i+'\" name=\"piece\" style=\"width:100%\" value=\"'+getVal+'\" onchange=\"checkPice(this.value,'+row["NOW_PIECE"]+','+row["TNUM"]+','+row["GNUM"]+','+i+',\''+row["LAB"]+'\')\" onkeyup=\"value=value.replace(\/[^\\-?\\d]/g,\'\')\" >',
                    			NUM:i,
                    			RK_NUM:rk,
                    			REMARKA: '<input type=\"text\" id=\"remark'+i+'\" name=\"remark\" style=\"width:100%\" value=\"'+remark+'\">' 
                    		}
                    	});
	            		if(getVal!=""){
	            			$('#infodg').datagrid('selectRecord',row["LAB"]);
	            		}
	            		
           		 	});//end each
	            	
	          }//end if 
	          
		},
		onUnselect:function(rowIndex,rowData){
			 if($("#piece"+rowIndex).val()!=""){
				 $('#infodg').datagrid('selectRecord',rowData["LAB"]);
			 }
		},
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false
	});
}


function getInfoRowObj(inforows,lab){
	var val="";
	if(inforows!=null && inforows.length>0){
		$(inforows).each(function(i){
			var obj=inforows[i];
    		if(lab==obj["LAB"]){
    			val=obj["PIECE"];
    		} 
	 	});
	}
	return val;
}

function getRemark(inforows,lab){
	console.log(lab);
	var val="";
	if(inforows!=null && inforows.length>0){
		$(inforows).each(function(i){
			var obj=inforows[i];
    		if(lab==obj["LAB"]){
    			val=obj["REMARK1"];
    		} 
	 	});
	}
	return val;
}
//判断 输入的出库件数
function checkPice(val,sum,tsum,gnum,i,lab){
	if(val!=""){
		if(Number(sum)<(Number(tsum)+Number(val))){
			$("#piece"+i).val("");
			$('#infodg').datagrid('unselectRow',i);
			parent.$.easyui.messager.alert("出库件数应小于库存件数减去静态质押件数！");
		}else if(Number(gnum)<Number(val)){
			$("#piece"+i).val("");
			$('#infodg').datagrid('unselectRow',i);
			parent.$.easyui.messager.alert("出库件数应小于等于要出库件数！");
		}else{
			$('#infodg').datagrid('selectRecord',lab);
		}
	}else{
		$('#infodg').datagrid('unselectRow',i);
	}
	
}
</script>
</body>
</html>