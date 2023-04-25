<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
	<div>
		<form id="infoForm" method="post" >
		</form>
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_goCard" class="easyui-validatebox" data-options="width:150,prompt: '出门证号'"/>
      	    <input type="text" name="filter_LIKES_carNum" class="easyui-validatebox" data-options="width:150,prompt: '卡车牌号'"/>
      	    <input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
	        <input type="text" name="filter_GED_appearTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '出场时间'"/>
	    -  <input type="text" name="filter_LED_appearTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '出场时间'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="gocard:container:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('集装箱出门明细','gocard/container/add')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="gocard:container:update">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="gocard:container:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="gocard:container:print">
      		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="printIt()">打印</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" onclick="applyIt()">申请</a>
		<span class="toolbar-item dialog-tool-separator"></span>
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var d;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
   	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/gocard/container/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	        {field:'goCard',title:'出门证号',sortable:true,width:150,halign:'center',align:'center'},
			{field:'goCardStatus',title:'出门证申请状态',sortable:true,width:150,halign:'center',align:'center',
				formatter : function(value, row, index) {
					if(value==1){return '已申请'}
				}
			},
 	        {field:'carNum',title:'卡车牌号',sortable:true,width:150,halign:'center',align:'center'},
 	        {field:'ctnNumOne',title:'集装箱号',sortable:true,width:150,halign:'center',align:'center'},
 	        {field:'ctnNumTwo',title:'集装箱号',sortable:true,width:200,halign:'center',align:'center'},
 	        {field:'ifEmpty',title:'空重',sortable:true,width:200,halign:'center',align:'center',
 	        	formatter : function(value, row, index) {
 	       			return value == "1" ? '空箱':'重箱';
 	        	}
 	        },
 	        {field:'theSize',title:'尺寸',sortable:true,width:200,halign:'center',align:'center'},
 	        {field:'appearTime',title:'出场时间',sortable:true,width:200,halign:'center',align:'center'},
 	        {field:'billNum',title:'提单号',sortable:true,width:200,halign:'center',align:'center'},
 	        {field:'shipNum',title:'船名/船次',sortable:true,width:200,halign:'center',align:'center'},
 	        {field:'remark',title:'备注',sortable:true,width:100,halign:'center'}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/gocard/container/delete/"+row.goCard,
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

//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
		window.parent.mainpage.mainTabs.addModule('出门证（集装箱）修改','gocard/container/update/' + row.goCard);
}

//打印
function printIt(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('集装箱出门证打印','gocard/container/print/' + row.goCard);
}
//申请
function applyIt(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;

	if(row.goCardStatus=='1' ){
		parent.$.messager.alert("提示","已申请出门证,不能重复申请");
		return;
	}
	parent.$.messager.confirm('提示', '是否发送出门证？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/gocard/container/apply/"+row.goCard,
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}

</script>
</body>
</html>