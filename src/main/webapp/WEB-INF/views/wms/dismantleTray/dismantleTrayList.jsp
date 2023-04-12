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
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_oldTrayCode" class="easyui-validatebox" data-options="width:150,prompt: '原托盘号'"/>
      	    <input type="text" name="filter_LIKES_newTrayCode" class="easyui-validatebox" data-options="width:150,prompt: '新托盘号'"/>
	        <select type="text" name="filter_EQS_dismantleType" class="easyui-combobox" data-options="width:150,prompt: '拆托类型'" >
	        	<option></option>
      	    	<option value='1'>web拆托</option>
      	    	<option value='2'>出库拣货拆托</option>
      	    	<option value='3'>货转拆托</option>
      	    	<option value='4'>装车回库拆托</option>
      	    </select>
	        <input type="text" name="filter_GED_dismantleTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '拆托开始日期'"/>
	      - <input type="text" name="filter_LED_dismantleTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '拆托结束日期'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cxdg()">查询</a>
		</form>
		<shiro:hasPermission name="wms:dismantle:chaituo">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-exchange" plain="true" onclick="add()">拆托</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var d;

document.onkeydown = function () {if(event.keyCode == 13){cxdg();}};

$(function(){   
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/dismantle/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'id',title:'id',hidden:true},    
	        {field:'oldTrayCode',title:'原托盘号',sortable:true,width:100},    
	        {field:'newTrayCode',title:'新托盘号',sortable:true,width:100},
	        {field:'num',title:'数量',sortable:true,width:100},
	        {field:'dismantleType',title:'拆托类型',sortable:true,width:100,
	        	formatter: function(value, row, index) {
					if (value == 1) {
						return 'web拆托';
					}
					if (value == 2) {
						return '出库拣货拆托';
					}
					if (value == 3) {
						return '货转拆托';
					}
					if (value == 4) {
						return '装车回库拆托';
					}
				}	
	        },
	        {field:'dismantleUser',title:'拆托人',sortable:true,width:100},
	        {field:'dismantleTime',title:'拆托时间',sortable:true,width:100}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//创建查询对象并查询
function cxdg(){
	var obj = $("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

//弹窗增加
function add() {
	d=$("#dlg").dialog({   
	    title: '拆托操作',    
	    width: 1000,    
	    height: 440,    
	    href:'${ctx}/wms/dismantle/dismantleForm',
	    maximizable:true,
	    modal:true
// 	    buttons:[{
// 			text:'确认',
// 			handler:function(){
// 				$("#mainform").submit(); 
// 			}
// 		},{
// 			text:'取消',
// 			handler:function(){
// 				d.panel('close');
// 			}
// 		}]
	});
}

</script>
</body>
</html>