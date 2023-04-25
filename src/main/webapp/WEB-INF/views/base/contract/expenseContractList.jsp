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
      	    <input type="text" name="filter_LIKES_contractNum" class="easyui-validatebox" data-options="width:150,prompt: '合同'"/>
      	    <input type="text" name="filter_LIKES_canvassionPerson" class="easyui-validatebox" data-options="width:150,prompt: '揽货人'"/>
      	    <input type="text" id="clientName" name="filter_LIKES_clientName" class="easyui-validatebox" data-options="width:150,prompt: '客户名称'"/>
	        <input type="text" name="filter_GED_signTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '合同开始日期'"/>
	      - <input type="text" id="expirationTimeInput" name="filter_LED_expirationTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '合同结束日期'"/>
	      		<select class="easyui-combobox"  name="filter_EQS_contractState" data-options="width:150,prompt:'审核&未审核'">
					<option value="">审核&未审核</option>
					<option value="1">已审核</option>
					<option value="0">未审核</option>
				</select>
	      		<select class="easyui-combobox"  name="expirationIndex" data-options="width:150,prompt:'过期查询'">
					<option value="0">全部</option>
					<option value="1">已过期</option>
					<option value="2">一月内过期</option>
					<option value="3">一月内到期</option>
				</select>				
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="base:contract:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('合同录入','base/contract/createContractForm')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="base:contract:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
        <shiro:hasPermission name="base:contract:update">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="base:contract:check">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-zoom" plain="true" onclick="check()">查看</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="base:contract:copy">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="toCopy()">复制</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="base:contract:pass">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="isOK()">审核</a>
	    	<span class="toolbar-item dialog-tool-separator"></span>
    	</shiro:hasPermission>
    	<shiro:hasPermission name="base:contract:pass">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="notOK()">取消审核</a>
	    	<span class="toolbar-item dialog-tool-separator"></span>
    	</shiro:hasPermission>
    	<shiro:hasPermission name="base:contract:export">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">合同导出</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="base:contract:exportinfo">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportInfo()">合同明细导出</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
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
	//客户下拉
	$('#clientName').combobox({
	   method: "GET",
	   url: "${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode: 'remote',
	   onLoadSuccess:function(){
// 			if(client != null && client != ""){
// 				$('#clientName').combobox("select", client);
// 			}
		}
	});
	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/base/contract/json', 
	    fit : true,
		fitColumns : true,
		nowrap:false,
		border : false,
		idField : 'contractNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'contractNum',title:'合同号',sortable:true},    
	        {field:'canvassionPerson',title:'揽货人',sortable:true},
	        {field:'clientName',title:'客户',sortable:true,width:200},
	        {field:'contractState',title:'合同状态',sortable:true,
	        	formatter : function(value, row, index) {
	       			return value == 1 ? '已审核':'未审核';
	        	}
	        },
	        {field:'ifMan',title:'报价类型',sortable:true,width:37,
	        	formatter : function(value, row, index) {
	        		if(value==0){
	        			return "机械报价";
	        		}else if(value==1){
	        			return "人工报价";
	        		}
	        	}
	        },
	        {field:'signTime',title:'签订时间',sortable:true,width:37},
	        {field:'expirationTime',title:'到期时间',sortable:true,width:37},
	        {field:'remark',title:'备注',sortable:true,width:400}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb',
	    rowStyler:function(index,row){   
	        if (row.expirationTime!=null && (new Date(row.expirationTime))<(new Date((new Date()).toLocaleDateString()))){   
	            return 'background-color:red;';   
	        }   
	    },
	     onSelect:function(rowIndex,rowData){
//          console.log( $("tr[datagrid-row-index="+rowIndex+"]").attr("style") );
//  		console.log( $("tr[style*='rgb(255, 228, 141)']").attr("datagrid-row-index") );
  			var tt=$("tr[style*='rgb(255, 228, 141)']").attr('datagrid-row-index');
 			var row = $('#dg').datagrid('getData').rows[tt];
 			if(typeof(row) != 'undefined'){
	 			if(row.expirationTime!=null && (new Date(row.expirationTime))<(new Date((new Date()).toLocaleDateString())) ){
					$("tr[style*='rgb(255, 228, 141)").prop("style","height: 26px; background-color:red");
	 			}else{
	 				$("tr[style*='rgb(255, 228, 141)").prop("style","height: 26px; background-color:#FFFFFF");
	 			}
			}
 			$("tr[datagrid-row-index="+rowIndex+"]").prop("style","height: 26px; background-color:#ffe48d");
        }   
	});
});

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if(row.contractState == 1){
		parent.$.messager.show({title: "提示", msg: "该合同已审核不允许删除！", position: "bottomRight" });
	}else{
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if (data){
				$.ajax({
					type:'get',
					url:"${ctx}/base/contract/deleteContract/"+row.contractNum,
					success: function(data){
						successTip(data,dg);
					}
				});
			} 
		});
	}
}

//审核
function isOK(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	$.ajax({
					type:'get',
					url:"${ctx}/base/contract/ifnoprice/"+row.contractNum,
					success: function(data){
						if(data != "success"){
							parent.$.messager.show({ title : "提示",msg: "该合同下有未维护价钱的明细，审核失败！", position: "bottomRight" });
							return;
						}else{
							parent.$.messager.confirm('提示', '您确定要审核通过？', function(data){
								if (data){
									$.ajax({
										type:'get',
										url:"${ctx}/base/contract/passOkContract/"+row.contractNum,
										success: function(data){
											successTip(data,dg);
										}
									});
								} 
							});
						}
					}
				});
		
}

//取消审核
function notOK(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '您确定要取消审核？', function(data){
								if (data){
									$.ajax({
										type:'get',
										url:"${ctx}/base/contract/passNoContract/"+row.contractNum,
										success: function(data){
											successTip(data,dg);
										}
									});
								} 
							});
		
}


//复制
function toCopy(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '您确定要复制该合同？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/contract/copyContract/"+row.contractNum,
				success: function(data){
					successTip(data,dg);
				}
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
	if(row.contractState == 1){
		parent.$.messager.show({ title : "提示",msg: "该合同已审核不允许修改！", position: "bottomRight" });
	}else{
		window.parent.mainpage.mainTabs.addModule('合同修改','base/contract/updateContractForm/' + row.contractNum);
	}
}

//查看
function check(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('合同查看','base/contract/checkContractForm/' + row.contractNum);
}

//导出
function exportExcel(){
 	var url = "${ctx}/base/contract/export";
 	$("#searchFrom").attr("action",url).submit();
}

//导出(带货物)
function exportInfo(){
	var url = "${ctx}/base/contract/exportwith";
 	$("#searchFrom").attr("action",url).submit();
}
</script>
</body>
</html>