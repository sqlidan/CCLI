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
       	        <select class="easyui-combobox" id="customId" name="filter_EQI_customId" data-options="width:150,prompt:'客户'">
					<option value=""></option>
				</select> 
				<input id="pzNum" name="filter_EQS_pzNum" class="easyui-validatebox"  data-options="width: 150,prompt:'凭证号'" />
				<select class="easyui-combobox" id="nType" name="filter_EQI_nType" data-options="width:150,prompt:'类型'">
					<option value=""></option>
					<option value="1">应收</option>
					<option value="2">应付</option>
				</select> 
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
			
	       	<shiro:hasPermission name="base:client:add">
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="delVerifi();">取消核销</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>
	       	 
        </div> 
        
  </div>
<table id="dg"></table> 
<div id="dlg"></div> 
<div id="postdatediv" style="display: none;">
<input type="hidden" id="postids" name="postids"/>
</div>   
<script type="text/javascript">
var dg;
var d;
var getTDate;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};
$(document).ready(function(){
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/cost/cancelafter/hxsjson', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'verifiNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[
			{field:'verifiNum',title:'verifiNum',hidden:true},
	        {field:'info',title:'操作',sortable:true,width:80},
	        {field:'customName',title:'客户名称',sortable:true,width:150},    
	        {field:'pzNum',title:'凭证号',sortable:true,width:100},
	        {field:'moneyRmb',title:'挂账/冲账金额',sortable:true,width:120},
	        {field:'crTime',title:'日期',sortable:true,width:100},
	        {field:'nType',title:'类型',sortable:true,width:40,formatter : function(value, row, index) {
	        	if(1==value){return "应收"}else{return "应付"}
	        }},
	        {field:'cType',title:'单据类型',sortable:true,width:40,formatter : function(value, row, index) {
	        	if(1==value){return "挂账"}else if(2==value){return "现结"}else if(3==value){return "冲账"}else{return ""}
	        }},
	        {field:'nState',title:'状态',sortable:true,width:40,formatter : function(value, row, index) {
	        	if(2==value){return "取消"}else{return "正常"}
	        }},
	        {field:'upTime',title:'取消日期',sortable:true,width:60},
	        {field:'upUser',title:'取消人',sortable:true,width:60},
	        {field:'postState',title:'发送状态',sortable:true,width:40,formatter : function(value, row, index) {
	        	if(3==value){return "发送成功"}else if(1==value){return "已发送"}else if(2==value){return "发送失败"}else{return "未发送"}
	        }},
	    ]],queryParams: {
	    	ntype:1
		},
	    onLoadSuccess:function(){
			var rows = $('#dg').datagrid('getRows');
			if(rows!=null && rows.length>0){
				var rowObj;
				var inHtml="";
				for(var i = 0; i<rows.length; i++){
					rowObj=rows[i];
					if(1==rowObj["nState"] && (0==rowObj["postState"]||2==rowObj["postState"])){
						inHtml="&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:postJD('"+rowObj["verifiNum"]+"',"+rowObj["cType"]+","+rowObj["nType"]+")\">发送</a>";	
					}else{
						inHtml="";
					}
					$('#dg').datagrid('updateRow',{
                		index:i,
                		row: {
                			info:"<a href=\"javascript:hxInfo('"+rowObj["verifiNum"]+"')\">明细</a>"+inHtml,
                		}
                	});
				}
			}
			//查询的结果集
			getTDate=$('#dg').datagrid('getData');
			$("#postids").val(getTDate.ids);
		},
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
	$('#customId').combobox({
		   method:"GET",
		   url:"${ctx}/base/client/getClientAll",
		   valueField: 'ids',
		   textField: 'clientName',
		   mode:'remote' 
   	});
});
//查询
function cx(){
	var obj = $("#searchFrom").serializeObject();
	dg.datagrid('load', obj);
}
//取消核销
function delVerifi(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if(2==row.nState){
		parent.$.easyui.messager.alert("该核销单已经取消！");
		return false;
	}
	parent.$.messager.confirm('提示', '取消后无法恢复您确定要取消？', function(data){
		if (data){
			$.ajax({
				type:'post',
				url:"${ctx}/cost/cancelafter/delete/"+row.verifiNum,
				success: function(data){
					if("error"==data){
						parent.$.easyui.messager.alert("取消核销失败！");
					}else{
						successTip(data,dg);
					}
				}
			});
		} 
	});
}
//核销明细
function hxInfo(num){
	if (num == "" || num == "null") {
		parent.$.easyui.messager.alert("缺少核销号！");
		return false;
	}
	window.parent.mainpage.mainTabs.addModule("核销明细","cost/cancelafter/hxinfo/" + num);
}
function postJD(code,cType,nType){
	$.ajax({
		type:'post',
		url:"${ctx}/cost/cancelafter/postjd/"+code+"/"+nType+"/"+cType,
		success: function(data){
			if("error"==data){
				parent.$.easyui.messager.alert("数据传送失败！");
			}else{
				successTip(data,dg);
			}
		}
	});
}

</script>
</body>
</html>