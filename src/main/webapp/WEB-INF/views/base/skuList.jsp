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
       	        <input type="text" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150,prompt: 'SKU'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		        
			</form>
			
	       	<shiro:hasPermission name="base:sku:add">
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>
	       	 
	        <shiro:hasPermission name="base:sku:update">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">修改</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	         <shiro:hasPermission name="base:sku:delete">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	         <shiro:hasPermission name="base:sku:into">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="into()">导入</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
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
	    url:'${ctx}/base/sku/listjson', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'skuId',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'skuId',title:'SKU_ID',sortable:false,width:50},    
//	        {field:'producingArea',title:'SKU描述',sortable:false,width:40},
			{field:'cargoName',title:'品名',sortable:false,width:50},
	        {field:'cargoState',title:'库存类型',sortable:false,
	        	formatter : function(value, row, index) {
	        		if(value==1){return '成品'}else if(value==2){return '箱损'}else{return '货损'}
	        	}
	        },
	        {field:'validityTime',title:'有效日期',sortable:false,width:50},
	        {field:'typeName',title:'大类',sortable:false,width:50},
	        {field:'className',title:'小类',sortable:false,width:50},
	        {field:'typeSize',title:'规格',sortable:false,width:50},
	        {field:'remark',title:'备注',sortable:false,width:50} 
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
		title="SKU添加";
		href='${ctx}/base/sku/create';
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
				if($("#isupdatesku").val()==2){
					parent.$.messager.confirm('提示', '若单毛、单重、品名发生改变，则其余数据中包含此SKU的也将发生变化，是否确认修改？', function(data){
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
	var href='${ctx}/base/sku/update/'+row.skuId;
	add("SKU修改",href);     
}
//查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/sku/delete/"+row.skuId,
				success: function(data){
					//successTip(data,dg);
					if("error"==data){
						parent.$.easyui.messager.alert("SKU号："+row.skuId+"被使用中 禁止删除！");
					}else{
						successTip(data,dg);
					}
				}
			});
		} 
	});
}
//导入
function into(){
	d=$("#dlg").dialog({   
	    title: "SKU导入",    
	    width: 450,    
	    height: 450,    
	    href:'${ctx}/base/sku/into',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform").submit(); 
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
}
</script>
</body>
</html>