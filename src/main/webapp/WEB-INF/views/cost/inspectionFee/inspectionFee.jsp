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
		<form id="infoForm" method="post" >
		</form>
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <select id="searchStock" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '客户'" >
      	    </select>
	        <input type="text" name="filter_GED_checkDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '查验开始日期'"/>
	    -  <input type="text" name="filter_LED_checkDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '查验结束日期'"/>
	        <select type="text" name="filter_EQS_ifLx" class="easyui-combobox" data-options="width:150,prompt: '是否零星'" >
	        	<option value=""></option>
      	    	<option value='1'>是</option>
      	    	<option value='0'>否</option>
      	    </select>
      	    <select type="text" name="filter_EQI_ifPass" class="easyui-combobox" data-options="width:150,prompt: '审核状态'" >
	        	<option value=""></option>
      	    	<option value='1'>已审核</option>
      	    	<option value='0'>未审核</option>
      	    </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="cost:inspection:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('查验管理','cost/inspecion/add')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="cost:inspection:update">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="cost:inspection:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="cost:inspection:print">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印作业委托单</a>
	       	<span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    
	     <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
         onclick="exportExcel()">Excel导出</a>
         <span class="toolbar-item dialog-tool-separator"></span>
	    
	     <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="copyIt()">复制</a>
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
	//客户
	   $('#searchStock').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
   	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/cost/inspecion/json', 
	    fit : true,
		fitColumns : false,
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
	        {field:'feeId',title:'查验单号',hidden:true},  
	        {field:'ifPass',title:'是否审核',width:80,sortable:true,
	        	 formatter : function(value, row, index) {
	         			return value == "1" ? '已审核':'未审核';
	        	 }
	  	    },    
 	        {field:'billNum',title:'提单号',sortable:true,width:150},
 	        {field:'clientName',title:'客户',sortable:true,width:200},
 	        {field:'ctnNum',title:'箱号',sortable:true,width:200},
 	        {field:'checkDate',title:'查验日期',sortable:true,width:130},
 	        {field:'ctnType',title:'箱型',sortable:true,width:80},
  	        {field:'checkType',title:'查验品类',sortable:true,
  	        	formatter : function(value, row, index) {
  	        		if(value == "1"){
  	        			return "水产";
  	        		}else if(value == "2"){
  	        			return "冻肉";
  	        		}else if(value == "3"){
  	        			return "水果";
  	        		}else{
  	        			return "其他";
  	        		}
 	        	}
  	        },
 	        {field:'ctnAmount',title:'箱量',sortable:true},
	 	    {field:'ifLx',title:'零星客户',sortable:true,
	  	       formatter : function(value, row, index) {
	         			return value == "1" ? '是':'否';
	  	       }
	  	    },
 	        {field:'balanceWay',title:'结算方式',sortable:true,
  	        	formatter : function(value, row, index) {
         			return value == "1" ? '月结':'现结';
  	        	}
  	        },
  	        /* {field:'ifJs',title:'是否结算',sortable:true,
  	        	formatter : function(value, row, index) {
         			return value == "0" ? '否':'是';
  	        	}
  	        }, */
  	        {field:'costAmount',title:'费用金额',sortable:true},
 	        {field:'operateTime',title:'操作日期',sortable:true,width:130},
 	        {field:'operatePerson',title:'操作人员',sortable:true,width:100},
 	        {field:'remark',title:'备注',sortable:true,width:100}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});
//复制功能
function copyIt(){
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
       window.parent.mainpage.mainTabs.addModule('查验修改','cost/inspecion/copyIt/'+row.feeId);
}

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if(row.ifPass == 1){
	  parent.$.messager.show({ title : "提示",msg: "此查验单已被审核，无法删除！", position: "bottomRight" });
	  return;
	}
	
	$.ajax({
		type:'get',
		url:"${ctx}/cost/inspecion/ifsave/"+row.feeId,
		dataType:"text",
		success: function(data){
			if(data == "hasInfo"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "此查验单有明细存在，无法删除！", position: "bottomRight"});
				return;
			}else{
				parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
					if (data){
						$.ajax({
							type:'get',
							url:"${ctx}/cost/inspecion/delete/"+row.feeId,
							success: function(data){
								successTip(data,dg);
							},
						});
					} 
				});
			}//end else
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
		window.parent.mainpage.mainTabs.addModule('查验修改','cost/inspecion/update/' + row.feeId);
}

//打印
function print(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('打印作业委托单','cost/inspecion/print/' + row.feeId);
}

//导出
function exportExcel() {
    var url = "${ctx}/cost/inspecion/exportList";
    $("#searchFrom").attr("action", url).submit();
}
</script>
</body>
</html>