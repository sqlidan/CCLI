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
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		</div>
	 <table id="infodg"></table>
<script type="text/javascript">

$(document).ready(function(){
	grid();
});

function cx(){
	grid();
}

function grid(){
	  var sign = $("#blSign").val();
	  $('#infodg').datagrid({    
		method: "get",
	    url:'${ctx}/bis/trayinfo/ctrayjson/'+sign, 
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
		remoteSort:false,
	    columns:[[   
	        {field:'STOCK_NAME',title:'存货方',sortable:false,width:80}, 
	        {field:'BILL_NUM',title:'提单号',sortable:true,width:80},
	        {field:'ASN',title:'ASN',sortable:true,width:80},
	        {field:'CTN_NUM',title:'箱号',sortable:true,width:80 },
	        {field:'SKU_ID',title:'SKU',sortable:true,width:120},
	        {field:'RKDH',title:'入库号',sortable:true,width:80},
	        {field:'CARGO_NAME',title:'品名',sortable:false,width:80},
	        {field:'ENTER_STATE',title:'入库类型',sortable:false,width:50,
	        	formatter : function(value, row, index) {
	        		if(value==0){return '成品'}else if(value==1){return '货损'}else if(value==2){return '包装破损'}
	        	}
	        },
	        {field:'NOW_PIECE',title:'库存件数',sortable:false,width:80},
	        {field:'TNUM',title:'静态质押件数',sortable:false,width:80},
	        {field:'TTNUM',title:'动态质押件数',sortable:false,width:80},
	        {field:'HAS',title:'已添加件数',sortable:false,width:50},
	        {field:'PIECE',title:'出库件数',sortable:false,width:50},
	        {field:'SALE_NUM',title:'销售号',sortable:false,width:80},
	        {field:'CODE_NUM',title:'抄码数',sortable:false,width:80},
	        {field:'NET_SINGLE',title:'',hidden:true},
	        {field:'GROSS_SINGLE',title:'',hidden:true},
	        {field:'BEFORE_STOCK_IN',title:'原货主id',hidden:true},
	        {field:'LAB',title:'',hidden:true},
	        {field:'NUM',title:'',hidden:true},
	    ]],
	    queryParams: {
	    	outLinkId:'${trayInfo.contactNum}',
	    	clientId:'${trayInfo.stockIn}',
	    	ckId:'${trayInfo.warehouseId}',
	    	getRk:$("#getRk").val(),
	    	getBill:$("#getBill").val(),
	    	getSku:$("#getSku").val(),
	    	getMr:$("#getMr").val()
		},
		onLoadSuccess:function(){
			var rows =  $('#infodg').datagrid('getRows');
			var inforows =  $('#dg').datagrid('getRows');
			 if (rows!=null && rows.length>0) {
	            	trNum=rows.length;
	            	$(rows).each(function(i){
	            		var row=rows[i];
	            		var getVal="";
	            		var getSale="";
	            		var hasIt ="";
	            		var getCode="";
	            		if(inforows!=null && inforows.length>0){
// 	            			hasIt=getInfoRowObj(inforows,row);
//	            			getSale=getSalesNum(inforows,row);
	            		}
	            		$('#infodg').datagrid('updateRow',{
                    		index:i,
                    		row: {
                    			PIECE: '<input type=\"text\" id=\"piece'+i+'\" name=\"piece\" style=\"width:100%\" value=\"'+getVal+'\"  onchange=\"checkPice(this.value,'+row["NOW_PIECE"]+','+row["HAS"]+','+row["TNUM"]+','+row["TTNUM"]+','+i+',\''+row["LAB"]+'\')\"  onkeyup=\"value=value.replace(\/[^\\-?\\d]/g,\'\')\" >',
                    			NUM:i,
                    			SALE_NUM: '<input type=\"text\" id=\"sale'+i+'\" name=\"sale\" style=\"width:100%\" value=\"'+getSale+'\">',
                    			CODE_NUM: '<input type=\"text\" id=\"codenum'+i+'\" name=\"codenum\" style=\"width:100%\" value=\"'+getCode+'\"  onchange=\"ischeckNum(this,'+i+')\"  onkeyup=\"value=value.replace(\/[^\\-?\\d]/g,\'\')\" >',
                    		}
                    	});
                    	if(getVal!=""){
	            			$('#infodg').datagrid('selectRecord',row["LAB"]);
	            		}
           		 	});
	          }
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
 
function getInfoRowObj(inforows,row){
	var val="";
	if(inforows!=null && inforows.length>0){
		$(inforows).each(function(i){
			var obj=inforows[i];
    		if(obj["billNum"]==row["BILL_NUM"] && obj["ctnNum"]==row["CTN_NUM"] && obj["skuId"]==row["SKU_ID"] && obj["enterState"]==row["ENTER_STATE"]&& obj["asn"]==row["ASN"]){
    			val +=Number(obj["outNum"]);
    		} 
	 	});
	}
	return val;
}

function getSalesNum(inforows,row){
	var val="";
	if(inforows!=null && inforows.length>0){
		$(inforows).each(function(i){
			var obj=inforows[i];
    		if(obj["billNum"]==row["BILL_NUM"] && obj["ctnNum"]==row["CTN_NUM"] && obj["skuId"]==row["SKU_ID"] && obj["enterState"]==row["ENTER_STATE"] && obj["asn"]==row["ASN"]){
    			if( typeof(obj["salesNum"]) != "undefined" && null!=obj["salesNum"]){
    				val=obj["salesNum"];
    			}else{
    				val="";
    			}
    		} 
	 	});
	}
	return val;
}
//判断 输入的出库件数
function checkPice(val,sum,has,tsum,ttsum,i,lab){
	if(val!=""){
		if(Number(sum)<(Number(tsum)+Number(val)+Number(ttsum)+Number(has))){
			$("#piece"+i).val("");
			$('#infodg').datagrid('unselectRow',i);
			parent.$.easyui.messager.alert("出库件数应小于库存件数减去质押件数！");
		}else{
			$('#infodg').datagrid('selectRecord',lab);
		}
	}else{
		$('#infodg').datagrid('unselectRow',i);
	}
}

//数字校验
function ischeckNum(val,i) {
    if (val.value) {
        if (!isNaN(val.value)) {
            
        } else {
            $("#codenum"+i).val("");
            myfm.isnum.select();
            return false;
        }
    }
}
</script>
</body>
</html>