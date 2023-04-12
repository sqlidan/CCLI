<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="库管人员信息" style="overflow-y:auto">
		<div   style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
      	     <select id="searchKG" name="kgPerson" class="easyui-combobox" data-options="width:150,prompt: '库管人员'">
            </select>
      	    <select id="searchLH" name="lhPerson" class="easyui-combobox" data-options="width:150,prompt: '理货人员'" >
      	    </select>
      	     <select id="searchCC" name="lhPerson" class="easyui-combobox" data-options="width:150,prompt: '叉车人员'" >
      	    </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="wms:forecast:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="add()">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:forecast:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
        </div>
	<table id="dg"></table> 
</div>
<div data-options="region:'south',split:true,border:false" title="组员信息"  style="height:200px">
 	<div  style="padding:5px;height:auto" class="datagrid-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
		<span class="toolbar-item dialog-tool-separator"></span>
	</div>  
<table id="dgg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg
var dd;
$(function(){   
	ajaxS();	
	gridDG();
});


//库管人员组查询
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/base/work/json', 
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
	    	{field:'id',title:'id',hidden:true},  
	        {field:'person',title:'库管人员',sortable:true,width:100},    
 	        {field:'lhNum',title:'理货人员数量',sortable:true,width:100},
 	        {field:'ccNum',title:'叉车人员数量',sortable:true,width:100}
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
	$('#searchLH').combobox({
	  	method: "GET",
        url: "${ctx}/system/user/getUserAll",
        valueField: 'name',
        textField: 'name',
        mode: 'remote' 
   	});
   	
   	$('#searchCC').combobox({
	  	method: "GET",
        url: "${ctx}/system/user/getUserAll",
        valueField: 'name',
        textField: 'name',
        mode: 'remote' 
   	});
   	
   	$('#searchKG').combobox({
	  	method: "GET",
        url: "${ctx}/system/user/getUserAll",
        valueField: 'name',
        textField: 'name',
        mode: 'remote' 
   	});
}

//组明细信息
function info(id){
	dgg=$('#dgg').datagrid({    
		method: "get",
	    url:'${ctx}/base/work/infoJson/'+id, 
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
		singleSelect:false,
	    columns:[[   
	    	{field:'id',hidden:true},  
	        {field:'person',title:'名称',sortable:true,width:100},    
 	        {field:'workType',title:'人员类型',sortable:true,width:100,
                    formatter: function (value, row, index) {
                        if (value == "1") {
                            return '库管人员';
                        }
                        else if (value == "2") {
                            return '理货人员';
                        }
                        else if (value == "3"){
                            return '叉车人员';
                        }
                    }
            }
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

function add(){
	d = $("#dlg").dialog({
            title: '增加库管员',
            width: 380,
            height: 380,
            href: '${ctx}/base/work/addKg',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                		$.ajax({
							type:'get',
							url:"${ctx}/base/work/checkKg/"+$("#addKg").combobox("getValue"),
							success: function(data){
								if(data=="success"){
									$("#mainform2").submit();
                        			d.panel('close');
								}else{
									parent.$.messager.show({ title : "提示",msg: "库管员不得重复！", position: "bottomRight" });
	  							    return;
								}
							}
						});
                    }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                     cx()
                }, 500);
            }
        });
}

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "未选择数据！", position: "bottomRight" });
	   return;
	}
	parent.$.messager.confirm('提示', '将删除库管人员及组内所有成员且无法恢复，您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/work/delete/"+row.id,
				success: function(data){
					successTip(data,dg);
					info(0);
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


//添加货物明细
function addInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
	   parent.$.messager.show({ title : "提示",msg: "请选择一个库管员！", position: "bottomRight" });
	   return;
	}
	dd=$("#dlg").dialog({   
    	title: '货物信息添加',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/base/work/addOther/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
			    $("#mainform3").submit(); 
			    dd.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				dd.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){info(row.id)},500);
		}
	});
}

 

//删除明细
function delInfo(){
        var del = dgg.datagrid('getSelected');
        if (del == null) {
            parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
            return;
        }
        var rows = dgg.datagrid('getSelections');
        var ids = [];
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].id);
        }
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/base/work/deleteInfo/" + ids,
				success: function(data){
					dgg.datagrid('clearSelections');
					successTip(data, dgg);
				}
			});
		} 
	});
}

</script>
</body>
</html>