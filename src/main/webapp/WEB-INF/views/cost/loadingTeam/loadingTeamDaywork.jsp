<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto;">
        <div>
        	<form id="searchFrom" action="">
       	        <input id="billNum" name="filter_LIKES_billNum" type="text"  class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
       	        <select id="searchClient" name="filter_LIKES_client" type="text" class="easyui-validatebox" data-options="width:150,prompt: '装卸队'"> </select>
       	        <input id="starTime" name="filter_GED_loadingDate" type="text"   class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width: 150,prompt:'装卸开始日期'" />
       	        <input id="endTime" name="filter_LED_loadingDate" type="text"   class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width: 150,prompt:'装卸结束日期'"  />
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>  
			</form>
			
	       	<shiro:hasPermission name="base:loadingTeam:add">
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>
	       	 
	        <shiro:hasPermission name="base:loadingTeam:update">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">修改</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="base:loadingTeam:delete">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="base:loadingTeam:finish">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="finish()">完成</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-lock-delete" plain="true" onclick="concel()">取消完成</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        
        </div> 
        
  </div>

<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">

	document.onkeydown = function () {if(event.keyCode == 13){cx();}};

	$(function(){
	//装卸队
	   $('#searchClient').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=2",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
   	
});	 


var dg;
var d;
$(document).ready(function(){
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/cost/loadingTeam/listjson', 
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
            {field:'id',hidden:true}, 
	        {field:'billNum',title:'提单号',sortable:false,width:100},    
	        {field:'client',title:'装卸队',sortable:false,width:180},
	        {field:'loadingDate',title:'装卸日期',sortable:false,width:100},
	        {field:'schemeName',title:'装卸队方案',sortable:false,width:150},
	        {field:'dayworkNum',title:'小时数',sortable:false,width:70},
	        {field:'dayworkRemark',title:'日工备注',sortable:false,width:100},
	        {field:'evevatorNum',title:'电梯工数',sortable:false,width:70},
	        {field:'evevatorRemark',title:'电梯工备注',sortable:false,width:100},
	        {field:'overtime',title:'加班小时',sortable:false,width:70},
	        {field:'overtimeRemark',title:'加班备注',sortable:false,width:100},
	        {field:'billDate',title:'账单年月',sortable:false,width:80},
	        {field:'state',title:'状态',sortable:true,width:70,
	 	           formatter : function(value, row, index) {
	 	        	  return value == "0" ? '未完成':'已完成';
	  	        	}
	 	    },
	        {field:'remark',title:'备注',sortable:false,width:100},
	        {field:'creater',title:'创建人',sortable:false,width:100},
	        {field:'createTime',title:'创建时间',sortable:false,width:120}
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
		title="装卸队日工添加";
		href='${ctx}/cost/loadingTeam/create';
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
//修改
function upd(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if( row.state == "1"){	
     	parent.$.messager.show({title: "提示", msg: "该日工单已完结，不可修改！", position: "bottomRight" });
     	return;
    }
	var href='${ctx}/cost/loadingTeam/update/'+row.id;
	add("装卸队日工修改",href);     
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
	if( row.state == "1"){	
     	parent.$.messager.show({title: "提示", msg: "该日工单已完结，不可删除！", position: "bottomRight" });
     	return;
    }
	parent.$.messager.confirm('提示', '删除后无法恢复，您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/cost/loadingTeam/delete/"+row.id,
				success: function(data){
						successTip(data,dg);
					}
			});
		} 
	});
}

//完成
function finish(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	
	if( row.state == "1"){	
     	parent.$.messager.show({title: "提示", msg: "该日工单已完结！", position: "bottomRight" });
     	return;
    }
	
 $.ajax({
		type : "get",
		url : "${ctx}/cost/loadingTeam/addstandbook/" + row.id,
		dataType : "text",
		success : function(msg) {
 			if(msg == "success"){
 				parent.$.messager.show({title: "提示", msg: "完结成功！", position: "bottomRight" });
 				cx();
 			}
		}
	});
}

//取消完成
function concel() {
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if( row.state == "0"){	
     	parent.$.messager.show({title: "提示", msg: "该日工单尚未完成，不可取消完成！", position: "bottomRight" });
     	return;
    }
	parent.$.messager.confirm('提示', '取消完成后将删除装卸队费用，确定要取消？', function (data) {
		if (data) {
		 $.ajax({
				type : "get",
				url : "${ctx}/cost/loadingTeam/concel/" + row.id,
				dataType : "text",
				success : function(msg) {
		 			if(msg == "success"){
		 				parent.$.messager.show({title: "提示", msg: "取消成功！", position: "bottomRight" });
		 				row.state="0";
		 				cx();
		 			}
				}
			});
		}
	});
}

</script>
</body>
</html>