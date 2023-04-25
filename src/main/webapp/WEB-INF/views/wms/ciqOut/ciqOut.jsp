<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="出库报检单总览" style="overflow-y:auto">
		<div style="padding:5px; height:auto" class="datagrid-toolbar" >
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
  <!--    	<select id="searchStock" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'" >
      	    </select>
      	    -->
      	    <input type="text" name="filter_LIKES_ciqCode" class="easyui-validatebox" data-options="width:150,prompt: '报检单号'"/>
	        <input type="text" name="filter_GED_declareTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申报开始时间'"/>
	        <input type="text" name="filter_LED_declareTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申报结束时间'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<form id="searchFrom3" action="">
		</form>
		<shiro:hasPermission name="wms:ciqout:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('出库报检管理','wms/ciqout/add')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:ciqout:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:ciqout:update">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:ciqout:export">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:ciqout:exportinfo">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportInfo()">导出（带货物信息）</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:ciqout:print">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印报检单统计</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
         <shiro:hasPermission name="wms:ciqout:printinfo">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="printInfo()">打印报检单货物信息统计</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
   <!--   
 <shiro:hasPermission name="wms:ciqout:wc">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="wc()">完成</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>-->
        </div>
	<table id="dg"></table> 
</div>
<div data-options="region:'south',split:true,border:false" title="出库报检货物信息汇总"  style="height:200px">
	<form id="searchFrom2" action="">
			<div style="padding:5px;height:auto" class="datagrid-toolbar">
      	    <input type="text" name="filter_LIKES_ciqNum" class="easyui-validatebox" data-options="width:150,prompt: 'HS编码'"/>
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

//入库报检单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/ciqout/json', 
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
	        {field:'ciqCode',title:'报检单号',sortable:true,width:20},    
 	        {field:'billNum',title:'提单号',sortable:true,width:20},
 	        {field:'certificateTime',title:'检验检疫证明日期',sortable:true,width:20},
 	        {field:'tradeType',title:'贸易方式',sortable:true,width:20},
 	        {field:'consignee',title:'收货人',sortable:true,width:20},
 	        {field:'consignor',title:'发货人',sortable:true,width:20},
 	        {field:'declareTime',title:'申报日期',sortable:true,width:20},
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



//入库报检货物信息
function info(id){
	dgg=$('#dgg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/ciqoutinfo/json/'+id, 
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
	    	{field:'ciqId',hidden:true}, 
 	        {field:'ciqNum',title:'HS编码',sortable:true,width:100},
 	        {field:'cargoName',title:'商品名称',sortable:true,width:100},
 	        {field:'scalar',title:'件数',sortable:true},
 	        {field:'netWeight',title:'净重',sortable:true},
 	        {field:'bagType',title:'包装种类',sortable:true},
 	        {field:'price',title:'货值',sortable:true},
 	        {field:'recordMan',title:'录入员',sortable:true},
 	        {field:'recordTime',title:'录入时间',sortable:true},
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
				url:"${ctx}/wms/ciqout/delete/"+row.id,
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
		window.parent.mainpage.mainTabs.addModule('出库报检单修改','wms/ciqout/update/' + row.id);
}

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

//创建查询对象并查询
function zx(){
	var obj=$("#searchFrom2").serializeObject();
	dgg.datagrid('load',obj); 
}

//导出
function exportExcel(){
 	var url = "${ctx}/wms/ciqout/export";
 	$("#searchFrom").attr("action",url).submit();
}

//导出(带货物)
function exportInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一条出库报检单数据！", position: "bottomRight" });
	   return;
	}
	var url = "${ctx}/wms/ciqout/exportwith/"+row.id;
 	$("#searchFrom3").attr("action",url).submit();
}


//打印
function print(){
	var rows = $('#dg').datagrid("getRows");
	var ids= new Array();
	for(var i=0;i<rows.length;i++){
		ids[i] = rows[i].id;
	}
	window.parent.mainpage.mainTabs.addModule('出库报检单打印','wms/ciqout/print/' + ids);
}

//带有货物信息的打印
function printInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一条出库报检单数据！", position: "bottomRight" });
	   return;
	}
	window.parent.mainpage.mainTabs.addModule('出库报检单（带货物信息）打印','wms/ciqout/printInfo/' + row.id);
}

</script>
</body>
</html>