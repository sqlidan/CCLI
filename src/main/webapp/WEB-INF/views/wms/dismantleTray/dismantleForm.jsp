<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tbb" style="padding:5px;height:auto">
	<div>
      	<form id="searchFroms" action="">
      		<input type="text" name="filter_LIKES_trayId" class="easyui-validatebox" data-options="width:150, prompt: '托盘号'"/>
  	        <input type="text" name="filter_LIKES_asn" class="easyui-validatebox" data-options="width:150, prompt: 'ASN'"/>
  	        <input type="text" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150, prompt: 'SKU'"/>
  	        <input type="text" id="clientName" name="filter_EQS_stockIn" class="easyui-validatebox" data-options="width:150, prompt: '客户'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cxs()">查询</a>
		</form>
 	</div> 
</div>
<table id="dgs"></table> 
<div id="dlgs"></div>

<form id="searchFromDis" action="">
	<div style="padding:10px;">
		 <input type="hidden" id="id"  name="id"/>
	         原托盘号:<input type="text" id="oldTray" name="oldTray" class="easyui-validatebox" data-options="width:150" style="background-color:#EBEBE4" readonly />
	         现有数量:<input type="text" id="oldNum" name="oldNum" class="easyui-validatebox" data-options="width:150" style="background-color:#EBEBE4" readonly />    
	   	 新托盘号:<input type="text" id="newTray" name="newTray" class="easyui-validatebox" data-options="width:150, required:'required' "/>
	         拆托数量:<input type="text" id="newNum" name="newNum" class="easyui-numberbox" data-options="width:150, required:'required'" validType="ddPrice[1,9999999]"  />
	  	<span class="toolbar-item dialog-tool-separator"></span>
	  	<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" onclick="comeCode()">确认拆托</a>
	</div>
</form>

<script type="text/javascript">
var dgg;
var dd;

$(function(){   
	//客户下拉
	$('#clientName').combobox({
	   method: "GET",
	   url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode: 'remote',
	   onLoadSuccess:function(){
		}
	});
	
	dgg = $('#dgs').datagrid({    
		method: "get",
	    url:'${ctx}/bis/trayinfo/jsonPage?filter_EQS_cargoState=01', 
	    height: 350,
	    fit : false,
		fitColumns : true,
		border : false,
		idField : 'id',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field: 'id',title:'id',hidden:true},    
	        {field: 'trayId',title:'托盘号',sortable:true,width:100},    
	        {field: 'asn',title:'ASN',sortable:true,width:100},
	        {field: 'skuId', title:'SKU', sortable:true, width:100},
	        {field: 'stockName',title:'客户名称', sortable:true,width:100},
	        {field: 'cargoLocation', title:'库位号', sortable:true, width:100},
	        {field: 'cargoName', title:'产品名称', sortable:true, width:100},
	        {field: 'nowPiece', title:'现有数量', sortable:true, width:100},
	        {field: 'netWeight', title:'净重', sortable:true, width:100},
	        {field: 'grossWeight', title:'毛重', sortable:true, width:100},
	        {field: 'units', title:'单位', sortable:true, width:100,
	        	formatter : function(value, row, index) {
	       			if(value == '1'){
	        	      	return '千克';
	        	    }
	        	    if(value == '2'){
	        	      	return '吨';
	        	    }
	        	 }	
	        },
	        {field: 'cargoState', title:'状态', sortable:true, width:100,
	        	formatter : function(value, row, index) {
	       			if(value == '00'){
	        	      	return '已收货';
	        	    }
	        	    if(value == '01'){
	        	      	return '已上架';
	        	    }
	        	    if(value == '10'){
	        	      	return '出库中';
	        	    }
	        	    if(value == '11'){
	        	      	return '出库理货';
	        	    }
	        	    if(value == '12'){
	        	      	return '已出库';
	        	    }
	        	    if(value == '20'){
	        	      	return '待回库';
	        	    }
	        	    if(value == '21'){
	        	      	return '回库收货';
	        	    }
	        	    if(value == '99'){
	        	      	return '货损';
	        	    }
	        	 }		
	        }
	    ]],
	    enableHeaderClickMenu: false,
	    enableHeaderContextMenu: false,
	    enableRowContextMenu: false,
	    toolbar:'#tbb',
	    onClickRow:function(rowIndex,rowData){
	    	$('#id').val(rowData.id);
	    	$('#oldTray').val(rowData.trayId);
	    	$('#oldNum').val(rowData.nowPiece);
	    }
	});
});

//创建查询对象并查询
function cxs(){
	var obj=$("#searchFroms").serializeObject();
	dgg.datagrid('load',obj); 
}

//确认拆托
function comeCode(){
	if ($("#searchFromDis").form('validate')) {
		
		var id = $('#id').val();
		var oldNum = $('#oldNum').val();
		var newTray = $('#newTray').val();
		var newNum = $('#newNum').val();
	
		if(id == ""){
			parent.$.messager.show({title: "提示", msg: "请选择一条库存数据！"});
		}else{
			if(eval(newNum) >= eval(oldNum)){
				parent.$.messager.show({title: "提示", msg: "拆托数量不得大于等于现有数量！"});
			}else{
				$.ajax({
					async: false,
					type: 'POST',
					data: "newTrayCode="+ newTray +"&num="+ newNum,
					url: "${ctx}/wms/dismantle/"+ id +"/dismantleTrayWebConfirm",
					success: function(data){
						if(data == "success"){
							parent.$.messager.show({title: "提示", msg: "操作成功！"});
							window.cxdg();
						}else{
							parent.$.messager.show({title: "提示", msg: "数据状态有误或新托盘已存在，请重新操作！"});
						}
						cxs();
						$('#oldNum').val(Number($('#oldNum').val()) - Number(newNum));
					}
				});
			}
		}
	}
}

</script>
</body>
</html>