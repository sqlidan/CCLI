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
       	        <select class="easyui-combobox" id="receiver" name="receiver" data-options="width:150,prompt:'客户',required:'required' ,">
					<option value=""></option>
				</select> 
       	         <input id="strTime" name="strTime" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt:'账单年月开始'"    />
       	         <input id="endTime" name="endTime" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt:'账单年月结束'"    />
		         <select class="easyui-combobox" id="ngz" name="ngz" data-options="width:150,prompt:'查询类型'">
					<option value="0"></option>
					<option value="1">所有</option>
					<option value="2">未挂账</option>
				</select> 
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
			
       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openVerifi(1);">挂账</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
     
       	 
       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openVerifi(2);">现结</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
       	 
       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openVerifi(3);">冲账</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
	       	 
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
	    url:'${ctx}/cost/cancelafter/listjson/1', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'IDS',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[
			{field:'IDS',title:'IDS',hidden:true},
	        {field:'info',title:'操作',sortable:true,width:40},
	        {field:'CUSTOMS_NUM',title:'CUSTOMS_NUM',hidden:true},    
	        {field:'CUSTOMS_NAME',title:'客户名称',sortable:true,width:100},    
	        {field:'BILL_DATE',title:'账单年月',sortable:true,width:100,
	        	formatter : function(value, row, index) {
	        		if(value!=null && value!="" && value.indexOf(":")>0){
	        			value=value.substring(0,value.lastIndexOf("-"));	
	        		}
	        		if(value!=null && value!="" && value.split("-").length==3){
	        			value=value.substring(0,value.lastIndexOf("-"));	
	        		}
	       			return value;
	        	}},
	        {field:'YSZ',title:'应收RMB',sortable:true,width:100},
	        {field:'YS',title:'实收RMB',sortable:true,width:100},
	        {field:'WS',title:'未收RMB',sortable:true,width:100}
	         
	    ]],
	    onLoadSuccess:function(){
			var rows = $('#dg').datagrid('getRows');
			if(rows!=null && rows.length>0){
				var rowObj;
				for(var i = 0; i<rows.length; i++){
					rowObj=rows[i];
					$('#dg').datagrid('updateRow',{
                		index:i,
                		row: {
                			info:"<a href=\"javascript:lookInfo("+rowObj["CUSTOMS_NUM"]+",'"+rowObj["BILL_DATE"]+"')\">查看明细</a>",
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
	$('#receiver').combobox({
		   method:"GET",
		   url:"${ctx}/base/client/getClientAll",
		   valueField: 'ids',
		   textField: 'clientName',
		   mode:'remote' 
   	});
});
var iscx=false;
var cxval=0;
//查询
function cx(){
	var receiver=$("#receiver").combobox("getValue");
	var strTime=$("input[name='strTime']").val();
	var endTime=$("input[name='endTime']").val();
	if(receiver=="" ){
		parent.$.easyui.messager.alert("请输入查询条件！");
	} else {
			cxval=$('#ngz').combobox("getValue");
			var obj = $("#searchFrom").serializeObject();
			dg.datagrid('load', obj);
			iscx=true;
	}
}
//查看明细
function lookInfo(customID, ym) {
	if (customID == "" || customID == null) {
		parent.$.easyui.messager.alert("该账单缺少客户，无法进行明细查看");
		return false;
	}
	if (ym == "" || ym == "null") {
		parent.$.easyui.messager.alert("该账单缺少账单年月，无法进行明细查看");
		return false;
	}
	window.parent.mainpage.mainTabs.addModule("账单明细",
			"cost/cancelafter/bkinfo/" + customID + "/" + ym + "/1");
}
	//打开核销添加页面
	function openVerifi(ctype) {
		if(1==ctype){
			var getcx=$('#ngz').combobox("getValue");
			if(getcx!=2 && cxval!=2){
				iscx=false;
				parent.$.easyui.messager.alert("请选择查询类型为未挂账进行查询后，再执行挂账操作！");
				return false;
			}
		}
		var ids = "";
		var rows = $('#dg').datagrid('getSelections');
		if (rows != null && rows.length > 0) {
			for (var i = 0; i < rows.length; i++) {
				ids += rows[i]["IDS"] + ",";
			}
			$("#postids").val(ids);
		}else{
			$("#postids").val(getTDate.ids);
		}
		//|| (rows != null && rows.length > 0) 
		if(true==iscx ){
			var str="挂账";
			if(2==ctype){
				str="现结";
			}else if(3==ctype){
				str="冲账";
			}
			d = $("#dlg").dialog({
				title : "添加"+str,
				width : 450,
				height : 450,
				href : '${ctx}/cost/cancelafter/open/1/'+ctype,
				maximizable : true,
				modal : true,
				buttons : [ {
					text : '确认',
					handler : function() {
						$("#mainform").submit();
					}
				}, {
					text : '取消',
					handler : function() {
						d.panel('close');
					}
				} ]
			});
		}else{
			parent.$.easyui.messager.alert("请输入条件查询台账信息进行核销操作！");
		}
		
	}
</script>
</body>
</html>