<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
			<form id="searchFrom" action="">
     	        <select  id="selcur" name="filter_EQS_currencyType" class="easyui-combobox" data-options="width:150,prompt: '币种'"/>
				</select>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
	       	<shiro:hasPermission name="base:taxrate:add">
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="add();">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>
	        <shiro:hasPermission name="base:taxrate:update">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="base:taxrate:delete">
        		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del()">删除</a>
        	</shiro:hasPermission>
        </div> 
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
$(document).keypress(function(e) {  
    // 回车键事件  
       if(e.which == 13) {  
   		cx(); 
       }  
   }); 
$(function(){   
	dg=$('#dg').datagrid({    
	method: "get",
    url:'${ctx}/base/taxrate/json', 
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
    	{field:'currencyType',title:'币种',sortable:true,width:100,
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
        {field:'exchangeRate',title:'汇率',sortable:true,width:100},    
        {field:'theDate',title:'日期',sortable:true,width:100}, 
        {field:'repairDate',title:'修改日期',sortable:true,width:100}
    ]],
    headerContextMenu: [
        {text: "冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", true).contains(field); },
         handler: function (e, field) { dg.datagrid("freezeColumn", field); }
        },
        {text: "取消冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", false).contains(field); },
         handler: function (e, field) { dg.datagrid("unfreezeColumn", field); }
        }
    ],
    enableHeaderClickMenu: true,
    enableHeaderContextMenu: true,
    enableRowContextMenu: false,
    toolbar:'#tb'
	});
	$.ajax({
	   type: "GET",
	   async:false,
	   url: "${ctx}/system/dict/json",
	   data: "filter_LIKES_type=currencyType",
	   dataType: "json",
	   success: function(date){
		   for(var i=0;i<date.rows.length;i++){
			   $('#selcur').combobox({
				   data : date.rows,
				   valueField:'value',
				   textField:'label',
				   editable:false
			   });
		   }
	   }
	});
});

//弹窗增加
function add() {
	d=$("#dlg").dialog({   
    title: '插入币种税率',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/base/taxrate/create',
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


//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/taxrate/delete/"+row.id,
				success: function(data){
					successTip(data,dg);
				}
			});
		} 
	});
}

//弹窗修改
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	d=$("#dlg").dialog({   
	    title: '修改费目代码',    
	    width: 380,    
	    height: 340,    
	    href:'${ctx}/base/taxrate/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'修改',
			handler:function(){
				$('#mainform').submit(); 
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

</script>
</body>
</html>