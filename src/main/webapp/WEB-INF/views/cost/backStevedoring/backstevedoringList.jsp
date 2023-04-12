<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
	<div>
		<form id="searchFrom" action="">
		    <span class="toolbar-item dialog-tool-separator"></span>
      	    <input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
      	    <select id="client" name="filter_LIKES_client" class="easyui-combobox" data-options="width:150,prompt: '装卸队'" >
      	    </select>
      	    <span class="toolbar-item dialog-tool-separator"></span>
      	    <select id="drostockinName" name="filter_LIKES_drostockIn" class="easyui-combobox" data-options="width:150,prompt: '收货方'" >
      	    </select>
      	    <span class="toolbar-item dialog-tool-separator"></span>
	        <select type="text" name="filter_EQI_ifOk" class="easyui-combobox" data-options="width:150,prompt: '状态'" >
	        	<option></option>
      	    	<option value='0'>未完成</option>
      	    	<option value='1'>已完成</option>
      	    </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
 		<shiro:hasPermission name="cost:bisPay:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('倒箱装卸管理','cost/backstevedoring/list')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="cost:bisPay:update">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission> 
	</div> 
</div>

<table id="dg"></table> 

<script type="text/javascript">
var dg;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	   //装卸队客户
	   $('#client').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
	
		//收货方客户
	   $('#drostockinName').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
   	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/cost/backstevedoring/jsonList', 
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
	        {field:'billNum',title:'提单号',sortable:true,width:80},
 	        {field:'client',title:'装卸队',sortable:true,width:100},
 	        {field:'feePlan',title:'装卸队方案',sortable:true,width:100},
 	        {field:'drostockIn',title:'收货方',sortable:true,width:100},
 	        {field:'ifOk',title:'状态',sortable:true,width:50,
 	           formatter : function(value, row, index) {
         			if(value=="1"){
         			  return '已完成';
         			}
         			else if(value=="0"){
         			 return '未完成';
         			}
  	        	}
  	        }
	    ]],
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

//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	 	window.parent.mainpage.mainTabs.addModule('倒库装卸管理','cost/backstevedoring/update/' + row.billNum); 
	//window.parent.mainpage.mainTabs.addModule('倒箱装卸管理','cost/backstevedoring/list/');
}


</script>
</body>
</html>