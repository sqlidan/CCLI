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
       	        <input type="text" name="filter_ID" class="easyui-validatebox" data-options="width:150,prompt: 'ID'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		         <input type="text" name="filter_PRINTID" class="easyui-validatebox" data-options="width:150,prompt: '父ID'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		        
			</form>
			
	      
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	      
	       	 
	       
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">修改</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	       
        </div> 
        
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;

//document.onkeydown = function () {if(event.keyCode == 13){cx();}}

$(document).ready(function(){
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/base/product/listjsonInfo', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'ID',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'ID',title:'id',sortable:false,width:50},    
			{field:'PNAME',title:'小类名',sortable:false,width:50},
	        {field:'PRINTID',title:'父id',sortable:false,width:50}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});

});
 
//添加
function add(title,href){
	if(typeof(href) =='undefined'){
		title="product添加";
		href='${ctx}/base/product/create';
	}
 	d=$("#dlg").dialog({   
	    title: title,    
	    width: 450,    
	    height:520,    
	    href:href,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				if($("#isupdateproduct").val()==2){
					parent.$.messager.confirm('提示', '若单毛、单重、品名发生改变，则其余数据中包含此product的也将发生变化，是否确认修改？', function(data){
						if (data){
							 $("#mainform").submit(); 
						} 
					});
				}else{
					$("#mainform").submit(); 
				}
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
} 
//修改
function upd(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var href='${ctx}/base/product/update/'+row.ID;
	add("货品小类修改",href);     
}
//查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

</script>
</body>
</html>