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
				<input type="text" name="filter_code" class="easyui-validatebox" data-options="width:150,prompt: '费目代码'"/>
       	        <input type="text" name="filter_nameC" class="easyui-validatebox" data-options="width:150,prompt: '费目名称'"/>
     	        <select type="text" id="selfee" name="filter_feeType" class="easyui-combobox" data-options="width:150,prompt: '费用类别'"/>
     	           <option value=""></option>
				</select>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
	       	<shiro:hasPermission name="base:feecode:input">
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="input();">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>
	       	<shiro:hasPermission name="base:feecode:copy">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" data-options="disabled:false" onclick="copy()">复制</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="base:feecode:update">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="base:feecode:delete">
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
    url:'${ctx}/base/feecode/json', 
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
    	{field:'ID',title:'ID',hidden:true}, 
    	{field:'YSCODE',title:'集团费目代码',sortable:true,width:100},    
        {field:'YSINFO',title:'集团费目名称',sortable:true,width:100}, 
        {field:'CODE',title:'费目代码',sortable:true,width:100},    
        {field:'NAME_C',title:'费目名称',sortable:true,width:100}, 
        {field:'NAME_E',title:'费目英文名称',sortable:true,width:100},
        {field:'NAME_INVOICE',title:'税目名称',sortable:true,width:100},
//      {field:'priceBase',title:'基础价格',sortable:true,width:100},  
        {field:'UNITLABLE',title:'计量单位',sortable:true,width:100},   
        {field:'TERMLABLE', title:'条件属性',sortable:true, width:100},
        {field:'CURRENCYLABLE',title:'币种',sortable:true,width:100},
        {field:'BUY_BILL',title:'是否买方承担',sortable:true,
        	formatter : function(value, row, index) {
       			return value==1?'是':'否';
        	}
        },
        {field:'SELL_BILL',title:'是否卖方承担',sortable:true,
        	formatter : function(value, row, index) {
       			return value==1?'是':'否';
        	}
        },
        {field:'FUKUANDAN',title:'是否业务付款单费目',sortable:true,
        	formatter : function(value, row, index) {
       			return value==1?'是':'否';
        	}
        },
        {field:'FEETYPELABLE',title:'费用类别',sortable:true}
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
	   url: "${ctx}/system/dict/getjson",
	   data: "filter_LIKES_type=feeType",
	   dataType: "json",
	   success: function(date){
		   for(var i=0;i<date.rows.length;i++){
			   $('#selfee').combobox({
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
function input() {
	d=$("#dlg").dialog({   
    	title: '插入费目代码',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/base/feecode/create',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				if($("#mainForm").form('validate')){
					if( Number($("#maxPrice").val()) > Number($("#minPrice").val()) || $("#maxPrice").val()=="" || $("#minPrice").val()==""){
						$("#mainform").submit(); 
					}else{
						parent.$.easyui.messager.show({title: "操作提示", msg: "上限必须大于下限！", position: "bottomRight"});
					}
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

//复制弹框
function copy(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	d=$("#dlg").dialog({   
		    title: '增加费目代码',    
		    width: 380,    
		    height: 340,    
		    href:'${ctx}/base/feecode/copy/'+row.ID,
		    maximizable:true,
		    modal:true,
		    buttons:[{
				text:'增加',
				handler:function(){
					if($("#mainForm").form('validate')){
						if( Number($("#maxPrice").val()) > Number($("#minPrice").val()) || $("#maxPrice").val()=="" || $("#minPrice").val()==""){
							$("#mainform").submit(); 
						}else{
							parent.$.easyui.messager.show({title: "操作提示", msg: "上限必须大于下限！", position: "bottomRight"});
						}
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

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/feecode/delete/"+row.ID,
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
	    href:'${ctx}/base/feecode/update/'+row.ID,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'修改',
			handler:function(){
				if($("#mainForm").form('validate')){
					if( Number($("#maxPrice").val()) > Number($("#minPrice").val()) || $("#maxPrice").val()=="" || $("#minPrice").val()==""){
						$("#mainform").submit(); 
					}else{
						parent.$.easyui.messager.show({title: "操作提示", msg: "上限必须大于下限！", position: "bottomRight"});
					}
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

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

</script>
</body>
</html>