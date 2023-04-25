<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="组管理总览" style="overflow-y:auto">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_groupName" class="easyui-validatebox" data-options="width:150,prompt: '组名称'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<form id="searchFrom3" action="">
		</form>
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('组管理新增','platform/group/manage/add')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>

        </div>
	<table id="dg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg


document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	gridDG();
});

//入库报关单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/platform/group/manage/json',
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
			{field: 'id', title: 'id', hidden:true},
	        {field:'groupName',title:'组名称',sortable:true,width:40},
	        {field:'operator',title:'创建人',sortable:true,width:20},
	        {field:'createdTime',title:'创建日期',sortable:true,width:20}
	    ]],
	    /* onClickRow:function(rowIndex, rowData){
	    	info(rowData.id);
	    }, */
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}


//删除
function del(){
	var row = dg.datagrid('getSelected');
	var id = row.id;
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/platform/group/manage/delete/"+id,
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
		window.parent.mainpage.mainTabs.addModule('组管理修改','platform/group/manage/update/' + row.id);
}

//创建查询对象并查询
function cx(){
	dg.datagrid('clearSelections');
	var obj=$("#searchFrom").serializeObject();

	dg.datagrid('load',obj); 
}




</script>
</body>
</html>