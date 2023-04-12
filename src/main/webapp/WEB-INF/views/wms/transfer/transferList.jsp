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
        		<input type="text" name="filter_LIKES_transferId" class="easyui-validatebox" data-options="width:150,prompt: '货转单号'"/>
       	        <select class="easyui-combobox" id="receiver" name="filter_EQS_receiver" data-options="width:150,prompt:'收货方'">
					<option value=""></option>
				</select> 
       	        <select class="easyui-combobox" id="stockIn" name="filter_EQS_stockInId" data-options="width:150,prompt:'存货方'">
					<option value=""></option>
				</select>
				<input type="text" name="filter_LIKES_operator" class="easyui-validatebox" data-options="width:150,prompt: '操作人员'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
			
	       	<shiro:hasPermission name="bis:transfer:add">
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>
	        <shiro:hasPermission name="bis:transfer:update">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">修改</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	         <shiro:hasPermission name="bis:transfer:delete">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="checkTransStateDel()">删除</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="bis:transfer:outstock">
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="outstock()">制作出库联系单</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
        	</shiro:hasPermission> 
	         <shiro:hasPermission name="bis:transfer:adjust">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-credit" plain="true" data-options="disabled:false" onclick="transferAdjust()">费用</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
        </div> 
        
  </div>
<table id="dg"></table> 
<div id="dlg"></div>
<div id="dtg"></div>
<div id="win"></div> 
<div>
	
</div> 
<script type="text/javascript">
var dg;
var d,dt;
var splitJson,tids;//用于记录需要拆分的托盘记录

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(document).ready(function(){
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/bis/transfer/listjson', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'orderNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'transferId',title:'货转单号',sortable:true,width:100},    
	        {field:'operator',title:'操作人员',sortable:true,width:100},    
	        {field:'receiverName',title:'收货方',sortable:true,width:100},
	        {field:'receiverLinker',title:'收货方联系人',sortable:true,width:100},
	        {field:'stockInId',title:'存货方Id',sortable:true,width:100},
	        {field:'stockIn',title:'存货方',sortable:true,width:100},
	        {field:'receiverOrg',title:'结算单位',sortable:true,width:100},
	        {field:'feePlan',title:'费用方案',sortable:true,width:100},
	        {field:'cdNum',title:'报关号',sortable:true,width:100},
	        {field:'auditingState',hidden:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
	//into 收货方
	$('#receiver').combobox({
		   method:"GET",
		   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
		   valueField: 'ids',
		   textField: 'clientName',
		   mode:'remote',
   	}); 
	//into 原货主
	$('#stockIn').combobox({
		   method:"GET",
		   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
		   valueField: 'ids',
		   textField: 'clientName',
		   mode:'remote',
   	}); 
});
//查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
} 
//添加
function add(title,href){
	var href='bis/transfer/create/000000';
	window.parent.mainpage.mainTabs.addModule('货转单管理',href,'icon-hamburg-cv');
} 
//修改
function upd(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var href='${ctx}/bis/transfer/update/'+row.transferId;
	window.parent.mainpage.mainTabs.addModule('货转单管理',href,'icon-hamburg-cv');    
}
//删除
function del(transferId){
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'post',
				url:"${ctx}/bis/transfer/delete/"+transferId,
				success: function(data){
					if("error"==data){
						parent.$.easyui.messager.alert("货转单号："+transferId+" 删除失败！");
					}else if("state"==data){
						parent.$.easyui.messager.alert("货转单号："+transferId+" 状态不允许删除！");
					}else{
						successTip(data,dg);
					}
				}
			});
		} 
	});
}

//制作出库联系单
function outstock(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	$.ajax({
				type:'get',
				url:"${ctx}/bis/transfer/createOutstock/"+row.transferId,
				success: function(data){
					var href='${ctx}/wms/outstock/updateoutstock/'+data;
					window.parent.mainpage.mainTabs.addModule('出库联系单管理',href,'icon-hamburg-cv');  
					parent.$.messager.show({ title : "提示",msg: "出库联系单制作成功！", position: "bottomRight" });  
				}
			});
}

//检测货转单状态是否可删除
function checkTransStateDel(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	$.post("${ctx}/bis/transfer/checkstate",{"transferId":row.transferId},function(data){
		if(data!=null ){
			if(2==data.end){
				parent.$.easyui.messager.alert("该货转单已经完成货转不允许删除！");
			}else{
				del(row.transferId);
			}
		}
	}, "json");
}

//打开策略选中页面
function opencl(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	//执行异步校验 该订单是否可以生成装车单
	$.post("${ctx}/bis/truck/iscreate", { "ordid": row.orderNum },function(data){
		if(data!=null){
			if("success"==data.endStr){
				createTruck(row.orderNum);
			}else if("hsuccess"==data.endStr){
				parent.$.messager.confirm('提示', '订单号：'+row.orderNum+'已生成过装车单，确定后之前生成的装车单信息将清空，不可还原。您确定要重新生成吗？', function(data){
					if(data){
						createTruck(row.orderNum);
					}
				});
			}else if("herror"==data.endStr){
				parent.$.easyui.messager.alert("订单号："+row.orderNum+"的装车单状态不允许重新生成装车单 ！");
			}else{
				parent.$.easyui.messager.alert("订单号："+row.orderNum+" 不允许生成装车单！");
			}
		}     
	}, "json");
}
//打开策略选择弹窗
function createTruck2(ordid){
	d=$("#dlg").dialog({   
	    title: '出库策略选择',    
	    width: 300,    
	    height: 300,    
	    href:'${ctx}/bis/loading/opencl/'+ordid,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$('#clform').submit(); 
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
}
function createTruck(ordid){
	d=$("#dlg").dialog({   
	    title: '出库策略选择',    
	    width: 1000,    
	    height:500,    
	    href:'${ctx}/base/stategy/check/'+ordid,
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
//打开托盘拆分页面
function openSplitT(ordid){
	dt=$("#dtg").dialog({   
	    title: '托盘拆分',    
	    width: 500,    
	    height: 300,    
	    href:'${ctx}/bis/loading/openct/'+ordid,
	    maximizable:true,
	    modal:true,
	    resizable:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$('#ctform').submit(); 
			}
		},{
			text:'返回策略选择',
			handler:function(){
				dt.panel('close');
				opencl(); 
			}
		},{
			text:'取消',
			handler:function(){
				dt.panel('close');
			}
		}]
	}); 
}

//进入  货转费用调整  
function transferAdjust(){
		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		var linkId = row.transferId;
		var stockId = row.stockInId;
		window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/transferList/' + linkId + "/" + stockId);
}
</script>
</body>
</html>