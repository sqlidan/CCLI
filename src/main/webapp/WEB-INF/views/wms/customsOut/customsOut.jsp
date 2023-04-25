
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="出库报关单总览" style="overflow-y:auto">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
  <!--    	<select id="searchStock" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'" >
      	    </select>
      	    -->
      	    <input type="text" name="filter_LIKES_cdNum" class="easyui-validatebox" data-options="width:150,prompt: '报关单号'"/>
	        <input type="text" name="filter_GED_declareTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申报开始时间'"/>
	        <input type="text" name="filter_LED_declareTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申报结束时间'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<form id="searchFrom2" action="">
		</form>
		<shiro:hasPermission name="wms:customsout:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('出库报关管理','wms/customsout/add')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:customsout:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:customsout:update">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:customsout:export">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:customsout:exportinfo">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportInfo()">导出（带货物信息）</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:customsout:print">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印报关单统计</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
         <shiro:hasPermission name="wms:customsout:printinfo">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="printInfo()">打印报关单货物信息统计</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
   <!--   
 <shiro:hasPermission name="wms:customs:wc">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="wc()">完成</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>-->
        </div>
	<table id="dg"></table> 
</div>
<div data-options="region:'south',split:true,border:false" title="出库报关货物信息汇总"  style="height:200px">
	<form id="searchFrom2" action="">
			<div style="padding:5px;height:auto" class="datagrid-toolbar">
      	    <input type="text" name="filter_LIKES_itemNum" class="easyui-validatebox" data-options="width:150,prompt: '项号'"/>
	        <input type="text" name="filter_GED_recordTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '录入开始时间'"/>
	        <input type="text" name="filter_LED_recordTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '录入结束时间'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="zx()">查询</a>
	        </div>
		</form>
<table id="dgg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg

document.onkeydown = function () {if(event.keyCode == 13){cx();}};


$(function(){   
	ajaxS();	
	gridDG();
});

//出库报关单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/customsout/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'forId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field:'id',hidden:true},  
	        {field:'cdNum',title:'报关单号',sortable:true,width:20},    
 	        {field:'billNum',title:'提单号',sortable:true,width:20},
 	        {field:'examNum',title:'审批号',sortable:true,width:20},
 	        {field:'tradeType',title:'贸易方式',sortable:true,width:20},
 	        {field:'importTime',title:'进口日期',sortable:true,width:20},
 	        {field:'vesselName',title:'船名',sortable:true,width:20},
 	        {field:'voyageNum',title:'航次',sortable:true,width:20},
 	        {field:'piece',title:'件数',sortable:true,width:20},
 	        {field:'netWeight',title:'净重',sortable:true,width:20}
	    ]],
	    onClickRow:function(rowIndex, rowData){
	    	info(rowData.id);
	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

//客户名称下拉
function ajaxS(){
	//客户
	   $('#searchStock').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote'
   	});
}



//出库报关货物信息
function info(id){
	dgg=$('#dgg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/customsoutinfo/json/'+id, 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'forId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field:'id',hidden:true}, 
	    	{field:'cusId',hidden:true}, 
	        {field:'itemNum',title:'项号',sortable:true,width:100},    
 	        {field:'cdNum',title:'HS编码',sortable:true,width:100},
 	        {field:'cargoName',title:'商品名称',sortable:true,width:100},
 	        {field:'spec',title:'规格',sortable:true},
 	        {field:'scalar',title:'件数',sortable:true},
 	        {field:'netWeight',title:'净重',sortable:true},
 	        {field:'units',title:'单位',sortable:true,
 	        	formatter : function(value, row, index) {
         			  return '千克';
  	        	}
 	        },
 	        {field:'destination',title:'目的地',sortable:true,width:100},
 	        {field:'unitPrice',title:'单价',sortable:true},
 	        {field:'totalPrices',title:'总价',sortable:true},
 	        {field:'currencyType',title:'币种',sortable:true,
 	        	formatter : function(value, row, index) {
	         		if(value == 0){
	        	    	return '人民币';
	        	    }
	       			if(value == 1){
	        	      	return '美元';
	        	    }
	        	    if(value == 2){
	        	      	return '日元';
	        	    }
	        	    if(value == '201'){
	 	        		return "阿尔及利亚第纳尔";
	 	        	}
	        	}		
 	        },
 	        {field:'freeLavy',title:'免征',sortable:true,
 	        	formatter : function(value, row, index) {
 	       			return value == "0" ? '不免征':'免征';
 	        	}
 	        },
 	        {field:'duty',title:'关税',sortable:true},
 	        {field:'remark1',title:'备注',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/customsout/delete/"+row.id,
				success: function(data){
					successTip(data,dg);
				}
			});
		} 
	});
}

//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
		window.parent.mainpage.mainTabs.addModule('出库报关单修改','wms/customsout/update/' + row.id);
}

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

function zx(){
	var obj=$("#searchFrom2").serializeObject();
	dgg.datagrid('load',obj); 
}

//导出
function exportExcel(){
 	var url = "${ctx}/wms/customsout/export";
 	$("#searchFrom").attr("action",url).submit();
}

//导出(带货物)
function exportInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一条出库报关单数据！", position: "bottomRight" });
	   return;
	}
	var url = "${ctx}/wms/customsout/exportwith/"+row.id;
 	$("#searchFrom2").attr("action",url).submit();
}


//打印
function print(){
	var rows = $('#dg').datagrid("getRows");
	var ids= new Array();
	for(var i=0;i<rows.length;i++){
		ids[i] = rows[i].id;
	}
	 	window.parent.mainpage.mainTabs.addModule('出库报关单打印','wms/customsout/print/' + ids);
}

//带有货物信息的打印
function printInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一条出库报关单数据！", position: "bottomRight" });
	   return;
	}
	window.parent.mainpage.mainTabs.addModule('出库报关单（带货物信息）打印','wms/customsout/printInfo/' + row.id);
}

</script>
</body>
</html>