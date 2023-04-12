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
      	    <input type="text" name="filter_ACCOUNT_ID" class="easyui-validatebox" data-options="width:150,prompt: '账户ID'"/>
      	    <input type="text" name="filter_CUSTOMER_NAME" class="easyui-validatebox" data-options="width:150,prompt: '货主名称'"/>
      	    <input type="text" name="filter_BILL_NUM" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <input type="text" name="filter_CTN_NUM" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
      	    <input type="text" name="filter_PNAME" class="easyui-validatebox" data-options="width:150,prompt: '货物名称'"/>      	     
      	    <select id="filter_STATE" name="filter_STATE" class="easyui-combobox"
                    data-options="width:80,prompt: '质押类型'">
                <option value=''>全部</option>
                <option value='0'>静态解押</option>
                <option value='1'>静态质押</option>
                <option value='4'>换货解押</option>
                <option value='5'>换货质押</option>                
            </select>
            <select id="filter_STATUS" name="filter_STATUS" class="easyui-combobox"
                    data-options="width:80,prompt: '质押状态'">
                <option></option>
                <option value=''>全部</option>
                <option value='0'>未生效</option>
                <option value='1'>已生效</option>
            </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="base:client:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               data-options="disabled:false" onclick="comfirm()">生效</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="base:client:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               data-options="disabled:false" onclick="refuse()">驳回</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
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
	    url:'${ctx}/pledge/comfirm/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'codeNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field: 'id', title: 'id', hidden: true},
	    	{field: 'TREND_ID', title: '质押编号', sortable:true,width:100},
 	        {field:'ACCOUNT_ID',title:'账户ID',sortable:true,width:100},
 	   /*      {field:'CUSTOMER_NUMBER',title:'货主账号',sortable:true,width:50}, */
 	       	{field:'CUSTOMER_NAME',title:'货主名称',sortable:true,width:120},
 	      	{field:'BILL_NUM',title:'提单号',sortable:true,width:100},
 	     	{field:'SKU_ID',title:'SKU',sortable:true,width:120},
 	     	{field:'CTN_NUM',title:'箱号',sortable:true,width:100},
 	    	{field:'PNAME',title:'货物名称',sortable:true,width:100},
 	        {field:'PLEDGE_NUMBER',title:'质押监管数量',sortable:true,width:100},
 	        {field:'PLEDGE_WEIGHT',title:'质押监管重量',sortable:true,width:100},
 	/*         {field:'COMPANY_NAME',title:'客户名称',sortable:true,width:100}, */
 	        {field:'CREATE_DATE',title:'创建时间',sortable:true,width:100},
  	        {field:'CONFIRM_STATUS',title:'质押状态',sortable:true,
  	        	formatter : function(value, row, index) {
  	        		if(value=="0"){
  	        			value="未生效"
  	        		}else if(value=="1"){
  	        			value="已生效"
  	        		}else if(value=="2"){
  	        			value="已驳回"
  	        		}
  	        		return value 
 	        	}
  	        },
  	      {field:'STATE',title:'质押类型',sortable:true,
  	        	formatter : function(value, row, index) {
  	        		if(value=="0"){
  	        			value="静态解押"
  	        		}else if(value=="1"){
  	        			value="静态质押"
  	        		}else if(value=="2"){
  	        			value="动态质押"
  	        		}else if(value=="3"){
  	        			value="动态解押"
  	        		}else if(value=="4"){
  	        			value="换货解押"
  	        		}else if(value=="5"){
  	        			value="换货质押"
  	        		}
  	        		return value 
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

//确认
function comfirm() {
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
    parent.$.messager.confirm('提示', '确定要生效吗？', function (data) {
    	if(row.CONFIRM_STATUS!=0){
    		parent.$.messager.confirm('提示', '该记录无法生效');
    		return
    	}else{
    		if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/pledge/comfirm/save/" + row.ID,
                    success: function (data) {
                    	successTip(data, dg);
                    	dg.datagrid('load'); 
                        
                    },
                    
                });
            }
    	}
    });
}

//驳回
function refuse() {
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
    parent.$.messager.confirm('提示', '确定要驳回吗？', function (data) {
    	if(row.CONFIRM_STATUS!=0){
    		parent.$.messager.confirm('提示', '该记录无法驳回');
    		return
    	}else{
    		if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/pledge/comfirm/refuse/" + row.ID,
                    success: function (data) {
                    	successTip(data, dg);
                    },
                });
            }
    	}
    });
}
</script>
</body>
</html>