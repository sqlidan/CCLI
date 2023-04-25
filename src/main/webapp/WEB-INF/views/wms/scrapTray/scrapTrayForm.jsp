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
  	        <input type="text" name="filter_LIKES_asn" class="easyui-validatebox" data-options="width:150, prompt: 'ASN'"/>
  	        <input type="text" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150, prompt: 'SKU'"/>
  	        <input type="text" id="stockIn" name="filter_LIKES_stockIn" class="easyui-validatebox" data-options="width:150, prompt: '客户'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cxs()">查询</a>
		</form>
 	</div> 
</div>

<table id="dgs"></table> 
<div id="dlgs"></div>

<form id="searchFromD" action="" >
	<div style="padding:10px;">
	         报损单号:<input type="text" id="scrapCode" name="scrapCode" value="${scrapCode }" class="easyui-validatebox" data-options="width:150" style="background-color:#EBEBE4" readonly />
<!-- 	   	 报损类型:<select class="easyui-combobox" id="scrapType" name="scrapType" data-options="width:150, required:'required'" > -->
<!-- 					<option value=""></option> -->
<!-- 				 </select> -->
		<shiro:hasPermission name="wms:scrap:one">
			<span class="toolbar-item dialog-tool-separator"></span>
	  		<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" onclick="comeCode(1)">普通报损</a>
		</shiro:hasPermission>
	  	<shiro:hasPermission name="wms:scrap:two">
		  	<span class="toolbar-item dialog-tool-separator"></span>
		  	<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" onclick="comeCode(2)">库内分拣报损</a>
	  	</shiro:hasPermission>
	</div>
</form>

<script type="text/javascript">
var dgg;
var dd;

$(function(){   
	//报损类型  下拉
// 	$.ajax({
// 	   	type: "GET",
// 	   	async : false,
// 		url: "${ctx}/system/dict/searchDict/scrapType",
// 	   	data: "",
// 	   	dataType: "json",
// 	   	success: function(date){
// 	   		for (var i = 0; i < date.length; i++) {
// 				$('#scrapType').combobox({
// 					data: date,
// 					valueField: 'value',
// 					textField: 'label',
// 					editable: false
// 				});
// 			}
// 		}
// 	});
	
	//客户下拉
	$('#stockIn').combobox({
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
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[   
            {field : 'ck',checkbox : true},
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
	        },
	        {field:'scrapState',title:'货损状态', 
	    		formatter : function(value, row, index) {
	    			var a ;
					$.ajax({
						async: false,
						type: 'get',
						url: "${ctx}/wms/scrap/showScrapTray/" + row.trayId ,
						success: function(data){
							if(data.length > 0){
								if(data[0].scrapState == 0){
									a = "待确认";
								}
							}
						}
			  		});
			  		return a;
	        	 }
	    	}
	    ]],
	    enableHeaderClickMenu: false,
	    enableHeaderContextMenu: false,
	    enableRowContextMenu: false,
	    toolbar:'#tbb'
	});
});

//创建查询对象并查询
function cxs(){
	var obj = $("#searchFroms").serializeObject();
	dgg.datagrid('load',obj); 
}

//确认报损
function comeCode(scrapType){
	if ($("#searchFromD").form('validate')) {
		var row = dgg.datagrid('getSelected');
		if(row == null) {
			parent.$.messager.show({title: "提示", msg: "请选择要货损的托盘！", position: "bottomRight" });
			return;
		}
		parent.$.messager.confirm('提示', '确认货损？货损待确认选择无效！', function(data){
			if (data){
				var scrapCode = $("#scrapCode").val();
// 				var scrapType = $("input[name='scrapType']").val();
				
				var newIdsList = [];
				var datas = dgg.treegrid('getSelections');
				for(var i=0; i < datas.length; i++){
					newIdsList.push(datas[i].id);
				}
	 			$.ajax({
	 				async: false,
	 				type: 'POST',
	 				data: JSON.stringify(newIdsList),
	 				contentType: 'application/json;charset=utf-8',
	 				url: "${ctx}/wms/scrap/"+ scrapCode +"/"+ scrapType +"/scrapTrayConfirm",
	 				success: function(data){
	 					var datas = dgg.treegrid('getSelections');
						for(var i=0; i < datas.length; i++){
							$('#dgs').datagrid("unselectRow", datas[i].id);
						}
	 					window.cxdg();
	 					$('#dgs').datagrid('reload');  
			            $.messager.alert("提示", data, 'info');
	 				}
	 			});
			} 
		});
	}
}

</script>
</body>
</html>