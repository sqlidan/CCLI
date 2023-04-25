<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tblx" style="padding:5px;height:auto">
        <div>
        	<form id="searchLinkFrom" action="" method="post">
        		<input type="text" id="clinkNum" name="filter_EQS_linkId" class="easyui-validatebox" data-options="width:150,prompt: '联系单号'"/>
       	        <input type="text" id="cbillNum" name="filter_EQS_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
       	        <input type="hidden" name="filter_EQS_ctnNum" class="easyui-validatebox" data-options="width:150,prompt: 'MR号'"/>
       	        <input type="hidden" name="filter_EQS_transNum" class="easyui-validatebox" data-options="width:150,prompt: '装车单号'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cxLink()">查询</a>
		      </form>
  		</div>
  </div>
<table id="dglx"></table> 
<div id="dlglx"></div>  
<script type="text/javascript">
var dglx;
$(function(){   
	dglx=$('#dglx').datagrid({    
		method: "post",
	    url:'${ctx}/wms/enterStock/jsonlist', 
	    fit : true,
		fitColumns : true,
		border : false,
 		idField : 'linkId',
		striped:true,
		pagination:false,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'CODENUM',title:'单号',sortable:true,width:100},    
 	        {field:'NTYPE',title:'类型',sortable:true,width:100,formatter : function(value, row, index) {
   				return value==0?'入库联系单':'货转联系单';
    		}},
    		{field:'ITEMNUM',title:'提单号',sortable:true,width:100},
    		{field:'IF_BONDED',title:'是否保税',hidden:true,width:100},        
 	        {field:'STOCK',title:'存货方',sortable:true,width:100},
 	        {field:'OPERATE_TIME',title:'创建时间',sortable:true,width:100},
 	       	{field:'STOCKID',title:'STOCKID',hidden:true},
	    ]], 
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tblx'
	});
});
function cxLink(){
	 var data=$("#searchLinkFrom").serializeObject();
	 var billNum=$("#cbillNum").val();
	 var linkNum=$("#clinkNum").val(); 
	 if(billNum=="" && linkNum==""){
		 parent.$.easyui.messager.alert("请填写查询条件！");
		 return false;
	 } 
	 $.ajax({
		   type: "post",
		   url: "${ctx}/wms/enterStock/jsonlist",
		   data: data,//"billNum="+$("#filter_LIKES_billNum").val()+"&ctnNum="+$("#filter_LIKES_ctnNum").val()+"&transNum="+$("#filter_LIKES_transNum").val(),
		   dataType: "json",
		   success: function(data){
			   //$('#dglx').datagrid('load',data);
			   var rows = $('#dglx').datagrid('getRows');
			   for(var i=rows.length;i>0;i++){
				   $('#dglx').datagrid('deleteRow',i+1); 
			   }
			   if(data!=null && data.length>0){
				   for(var i=0;i<data.length;i++){
					   addLinkTBRow(data[i]);
				   }
			   }
		   }
	 });
}
//给Link表单添加信息
function addLinkTBRow(jsonObj){
	var rows = $('#dglx').datagrid('getRows');
	$('#dglx').datagrid('insertRow',{
		index: rows.length+1,
		row: {
			CODENUM:jsonObj.CODENUM,
			NTYPE:jsonObj.NTYPE,
			IF_BONDED:jsonObj.IF_BONDED,
			STOCK:jsonObj.STOCK,
			OPERATE_TIME:jsonObj.OPERATE_TIME,
			ITEMNUM:jsonObj.ITEMNUM,
			STOCKID:jsonObj.STOCKID
		}
	});
	
}
</script>
</body>
</html>