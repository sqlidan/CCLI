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
      	    <input type="text" id="thebillNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
      	<shiro:hasPermission name="wms:extension:later">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="later()">延期</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
	</div> 
</div>

<table id="dg"></table> 

<script type="text/javascript">
var dg;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
   	grid();
});

function grid(){
	var billNum = $("#thebillNum").val();
	if(billNum == "" || typeof(billNum)=="undefined"){
		billNum = 0;
	}
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/extension/json/'+billNum, 
	    fit : true,
		fitColumns : false,
		border : false,
		idField : 'linkId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field:'ID',title:'id',hidden:true},
	    	{field:'CDNUM',title:'报关单号',sortable:true,width:150},
	    	{field:'WARNINGDAY',title:'预警间隔',hidden:true},
 	        {field:'OUTWARNINGDAY',title:'弹出预警间隔',hidden:true},
	        {field:'ZTYPE',title:'类型',sortable:true,width:150,
	        	formatter : function(value, row, index) {
         			if(value=="11"){
         			  return '入库报关';
         			}
         			else if(value=="12"){
         			 return '出库报关';
         			}
         			else if(value=="21"){
         			 return '入库报检';
         			}
         			else if(value=="22"){
         			return '出库报检';
         			}
         			else{
         			return '类型错误';
         			}
  	        	}
 	        },    
 	        {field:'BILLNUM',title:'提单号',sortable:true,width:150},
 	        {field:'CLIENTNAME',title:'客户',sortable:true,width:150},
 	        {field:'DECLARETIME',title:'申报时间',sortable:true,width:200},
 	        {field:'RELEASETIME',title:'放行时间',sortable:true,width:100},
 	        {field:'DAYS',title:'预警天数',sortable:true,width:100},
  	        {field:'EXTENSION',title:'延期次数',sortable:true,width:100}
	    ]],
//	    rowStyler:function(index,row){
//	    	if( row.DAYS != null){ 
//				if ( parseInt(row.DAYS) <= parseInt(row.WARNINGDAY)){
//					return 'background-color:red;font-weight:bold;';
//				}
//			}
//		},
//	    onLoadSuccess:function(){
//	    	var rows = $('#dg').datagrid("getRows");
//	    	for(var i=0;i<rows.length;i++){
//	    		if(rows[i].DAYS != null){
// 					if( parseInt(rows[i].DAYS) < parseInt(rows[i].WARNINGDAY)){
// 						return 'background-color:red;';     
 //					}else{
 //						return 'background-color:red;';   
 //					}
//				}
//			}
//	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

//延期操作
function later(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '确定要进行延期操作？', function(data){
		if(data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/extension/later/"+row.ID+"/"+row.ZTYPE,
				success: function(data){
					if(data == "success"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "延期成功！", position: "bottomRight"});
						successTip(data,dg);
					}else if(data == "false"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "延期失败,已到最大延期次数！", position: "bottomRight"});
						return;
					}else{
						parent.$.easyui.messager.show({title: "操作提示", msg: "数据存在错误，延期失败！", position: "bottomRight"});
						return;
					}
				}
			});
		}
	})
}



//创建查询对象并查询
function cx(){
	grid();
}

</script>
</body>
</html>