<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="出库预报单列表" style="overflow-y:auto">
<div   style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <select id="searchStock" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'" >
      	    </select>
      	    <input type="text" name="filter_LIKES_declarationUnit" class="easyui-validatebox" data-options="width:150,prompt: '报关公司'"/>
      	    <input type="text" name="filter_LIKES_ciqDeclarationUnit" class="easyui-validatebox" data-options="width:150,prompt: '报检公司'"/>
	        <input type="text" name="filter_GED_createTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '创建开始日期'"/>
	        <input type="text" name="filter_LED_createTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '创建结束日期'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
      	<shiro:hasPermission name="wms:outforecast:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('出库预报单管理','wms/outforecast/create')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	    <shiro:hasPermission name="wms:enterStock:update">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:outforecast:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
         <shiro:hasPermission name="wms:outforecast:submitCD">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="submitCd()">生成出库报关单</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outforecast:submitCIQ">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="submitCiq()">生成出库报检单</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
   <!--     <shiro:hasPermission name="wms:outforecast:makelink">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-zoom  " plain="true" onclick="makeLink()">生成出库联系单</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission> -->
        <shiro:hasPermission name="wms:outforecast:print">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outforecast:export">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        </div>
	<table id="dg"></table> 
</div>
<div data-options="region:'south',split:true,border:false" title="出库预报单货物信息"  style="height:200px">
<!--	<div  style="padding:5px;height:auto" class="datagrid-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="copyInfo()">复制</a>
	</div> -->
<table id="dgg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	ajaxS();	
	gridDG();
});

//出库预报单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/outforecast/json', 
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
	    	{field:'forId',title:'出库预报单ID',sortable:true,width:200},  
	        {field:'billNum',title:'提单号',sortable:true,width:200},    
 	        {field:'clientName',title:'客户名称',sortable:true,width:200},
 	        {field:'ctnCont',title:'箱量',sortable:true,width:100},
 	        {field:'declarationUnit',title:'报关公司',sortable:true,width:200},
 	        {field:'ciqDeclarationUnit',title:'报检公司',sortable:true,width:200},
 	        {field:'cdNum',title:'报关单号',sortable:true,width:100},
 	        {field:'ciqNum',title:'报检单号',sortable:true,width:100},
 	        {field:'cdSign',title:'报关状态',sortable:true,
  	        	formatter : function(value, row, index) {
 	       			return value == "0" ? '未通关':'已通关';
 	        	}
  	        },
  	        {field:'ciqSign',title:'报检状态',sortable:true,
  	        	formatter : function(value, row, index) {
 	       			return value == "0" ? '未报检':'已报检';
 	        	}
  	        },
 	        {field:'tradeMode',title:'贸易方式',sortable:true},
 	        {field:'createTime',title:'创建时间',sortable:true,width:100}
	    ]],
	    onClickRow:function(rowIndex, rowData){
	    	info(rowData.forId);
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
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
}

//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('出库预报单修改','wms/outforecast/update/' + row.forId);
}

//货物信息
function info(forId){
	dgg=$('#dgg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/outforecastinfo/json/'+forId, 
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
	        {field:'billNum',title:'提单号',sortable:true,width:100},    
 	        {field:'hscode',title:'海关编码',sortable:true,width:100},
 	        {field:'cargoName',title:'品名',sortable:true,width:100},
 	        {field:'space',title:'规格',sortable:true},
 	        {field:'piece',title:'件数',sortable:true},
 	        {field:'netWeight',title:'净重',sortable:true},
 	        {field:'clearStore',title:'清库/清品名',sortable:true},
 	        {field:'trayNum',title:'木托数量',sortable:true,width:100},
 	        {field:'remark',title:'备注',sortable:true}
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
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一条出库预报单数据！", position: "bottomRight" });
	   return;
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/outforecast/delete/"+row.forId,
				success: function(data){
					successTip(data,dg);
				},
			});
		} 
	});
}



//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

//打印
function print(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一条出库预报单数据！", position: "bottomRight" });
	   return;
	}
	window.parent.mainpage.mainTabs.addModule('出库预报单打印','wms/outforecast/print/' + row.forId);
}

//导出
function exportExcel(){
 	var url = "${ctx}/wms/outforecast/export";
 	$("#searchFrom").attr("action",url).submit();
}

//添加货物明细
function addInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一条出库预报单数据！", position: "bottomRight" });
	   return;
	}
	d=$("#dlg").dialog({   
    	title: '货物信息添加',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/wms/outforecastinfo/create/'+row.forId,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
			    $("#mainform3").submit(); 
			    d.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){info(row.forId)},100);
		}
	});
}

//修改
function editInfo(){
	var row = dgg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	d=$("#dlg").dialog({   
    	title: '修改货物信息',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/wms/outforecastinfo/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
			   $("#mainform3").submit(); 
			   d.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){info(row.forId)},100);
		}
	});
	
}

//删除明细
function delInfo(){
	var row = dgg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/outforecastinfo/deleteinfo/" + row.id,
				success: function(data){
					dgg.datagrid('clearSelections');
					successTip(data, dgg);
				}
			});
		} 
	});
}

//复制弹框
function copyInfo(){
var row = dgg.datagrid('getSelected');
if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
d=$("#dlg").dialog({   
    	title: '增加货物信息',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/wms/outforecastinfo/copy/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
			   $("#mainform3").submit(); 
			   d.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){info(row.forId)},100);
		}
	});
}


//生成报关单
function submitCd(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	if(row.cdNum =="" || row.cdNum == null){
		parent.$.messager.show({title: "提示", msg: "没有报关单号，无法生成报关单！", position: "bottomRight" });
		return;
	}
	$.ajax({
				type: 'get',
				url: "${ctx}/wms/outforecast/customs/" + row.forId,
				success: function(data){
					if(data == "success"){
						successTip(data, dg);
					}else{
						parent.$.messager.show({title: "提示", msg: "出库报关单中存在此提单号数据！生成报关单失败！", position: "bottomRight" });
						return;
					}
				}
			});
}

//生成报检单
function submitCiq(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	if(row.ciqNum =="" || row.ciqNum == null){
		parent.$.messager.show({title: "提示", msg: "没有报检单号，无法生成报检单！", position: "bottomRight" });
		return;
	}
	$.ajax({
				type: 'get',
				url: "${ctx}/wms/outforecast/ciq/" + row.forId,
				success: function(data){
					if(data == "success"){
						successTip(data, dg);
					}else{
						parent.$.messager.show({title: "提示", msg: "出库报检单中存在此提单号数据！生成报检单失败！", position: "bottomRight" });
						return;
					}
				}
			});
}
</script>
</body>
</html>