<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="保税货物底账" style="overflow-y:auto">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">


			<form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_clientName" class="easyui-validatebox" data-options="width:150,prompt: '客户名称'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_cdNum" class="easyui-validatebox" data-options="width:150,prompt: '报关单号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
		<%--	<input type="text" name="filter_LIKES_ctnNum" class="easyui-validatebox" data-options="width:150,prompt: 'MR/集装箱号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>--%>
			<input type="text" name="filter_EQS_customerServiceName" class="easyui-validatebox" data-options="width:150,prompt: '所属客服'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_hsCode" class="easyui-validatebox" data-options="width:150,prompt: 'hs编码'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_LIKES_hsItemname" class="easyui-validatebox" data-options="width:150,prompt: '海关品名'"/>
			<span class="toolbar-item dialog-tool-separator"></span>

			<input type="text" name="filter_LIKES_accountBook" class="easyui-validatebox" data-options="width:150,prompt: '账册商品序号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>

		<%--	<input type="text" name="filter_GED_storageDate" class="easyui-my97" datefmt="yyyy-MM-dd"
				   data-options="width:150,prompt: '入库时间（开始）'"/>
			<input type="text" name="filter_LED_storageDate" class="easyui-my97" datefmt="yyyy-MM-dd"
				   data-options="width:150,prompt: '入库时间（结束）'"/>--%>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<form id="searchFrom3" action="">
		</form>
<%--	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="add()">添加</a>--%>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('保税货物底账添加','supervision/bonded/add')">添加</a>

	      	<span class="toolbar-item dialog-tool-separator"></span>
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
			   onclick="exportExcel()">导出EXCEL</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="startTransfer()">开始转非保税</a>
			<span class="toolbar-item dialog-tool-separator"></span>

        </div>
	<table id="dg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg


document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	gridDG();
});

//入库报关单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/bonded/json',
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'forId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[
			{field: 'id', title: 'id', hidden:true},
			{field: 'clientId', title: '客户ID', hidden:true},
	        {field:'clientName',title:'客户名称',sortable:true,width:70},
			{field:'billNum',title:'提单号',sortable:true,width:40},
			{field:'cdNum',title:'报关单号',sortable:true,width:70},
			//{field:'ctnNum',title:'MR/集装箱号',sortable:true,width:50},

			//{field:'itemName',title:'货物描述',sortable:true,width:70},
			//{field:'piece',title:'件数',sortable:true,width:20},

			//{field:'netWeight',title:'总净值',sortable:true,width:30},
			{field:'customerServiceName',title:'所属客服',sortable:true,width:30},
			{field:'hsCode',title:'hs编码',sortable:true,width:40},

			{field:'hsItemname',title:'海关品名',sortable:true,width:40},
			{field:'accountBook',title:'账册商品序号',sortable:true,width:20},

			{field:'dclQty',title:'申报重量',sortable:true,width:20},
			{field:'dclUnit',title:'申报计量单位',sortable:true,width:20},

			{field:'hsQty',title:'海关库存重量',sortable:true,width:20},
			{field:'typeSize',title:'规格',sortable:true,width:20},
			{field:'cargoLocation',title:'库位号',sortable:true,width:50},
			{field:'cargoArea',title:'库区',sortable:true,width:50},

			//{field:'storageDate',title:'入库时间',sortable:true,width:40},
			{field:'createdTime',title:'创建日期',sortable:true,width:40},
			{field:'updatedTime',title:'修改时间',sortable:true,width:40}
	    ]],
	    /* onClickRow:function(rowIndex, rowData){
	    	info(rowData.id);
	    }, */
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}


//删除
function del(){
	var row = dg.datagrid('getSelected');
	var id = row.id;
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/supervision/bonded/delete/"+id,
				success: function(data){
					successTip(data,dg);
				}
			});
		} 
	});
}
//新增
function add(){

	d=$("#dlg").dialog({
		title: '保税货物新增',
		width: 500,
		height: 340,
		href:'${ctx}/supervision/bonded/add',
		maximizable:true,
		modal:true,
		buttons:[{
			text:'新增',
			handler:function(){

				submitForm();
				if(result=="success"){
					d.panel('close');
					refresh();
				}

			}

			},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
	//window.parent.mainpage.mainTabs.addModule('操作员新增','platform/user/manage/add');
}
//修改
function update(){	
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
/*	d=$("#dlg").dialog({
		title: '保税货物修改',
		width: 500,
		height: 340,
		href:'${ctx}/supervision/bonded/update/'+row.id,
		maximizable:true,
		modal:true,
		buttons:[{
			text:'修改',
			handler:function(){

				submitForm();
				if(result=="success"){
					d.panel('close');
					refresh();
				}

				//cx();
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});*/
		window.parent.mainpage.mainTabs.addModule('保税货物底账修改','supervision/bonded/update/' + row.id);
}

//开始转非保税
function startTransfer() {
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	console.log(" row.id==>>"+ row.id)
	d = $("#dlg").dialog({
		title: "保税转非保税",
		width: 450,
		height: 450,
		href: '${ctx}/supervision/bonded/startTransfer/' + row.id,
		maximizable: true,
		modal: true,
		buttons: [{
			text: '确认',
			handler: function () {
				$("#mainform3").submit();
			}
		}, {
			text: '取消',
			handler: function () {
				d.panel('close');
			}
		}]
	});
}



function refresh(){
	window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
}

//创建查询对象并查询
function cx(){
	dg.datagrid('clearSelections');
	var obj=$("#searchFrom").serializeObject();

	dg.datagrid('load',obj);
}

//导出excel
function exportExcel(){
	var obj=$("#searchFrom").serializeObject();

	/*var strTime= obj["filter_GED_storageDate"];
	var endTime= obj["filter_LED_storageDate"] ;

	console.log("strTime",strTime);
	console.log("endTime",endTime);
	if(!strTime || !endTime){
		parent.$.messager.show({ title : "提示",msg: "请选择时间起止范围", position: "bottomRight" });
		return;
	}
	if((new Date(endTime)-new Date(strTime))/(1000*60*60*24)>30){
		parent.$.messager.show({ title : "提示",msg: "时间范围不要大于30天", position: "bottomRight" });
		return;
	}*/

	var url = "${ctx}/supervision/bonded/exportExcel";
	$("#searchFrom").attr("action",url).submit();
	//window.location.href = url;
}


</script>
</body>
</html>