<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="底账明细列表" style="overflow-y:auto">
<div   style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">

			<input type="text" name="filter_billNo" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_containerNo" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="text" name="filter_appointDateStart" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '入库日期（开始）'"/>
			<input type="text" name="filter_appointDateEnd" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '入库日期（结束）'"/>
			<span class="toolbar-item dialog-tool-separator"></span>

			<select id="status" name="filter_status" class="easyui-combobox"
					data-options="width:150,prompt: '状态'">
				<option></option>
				<option value="0">已保存</option>
				<option value="1">已入闸</option>
				<option value="2">已出闸</option>
				<option value="3">已取消</option>
			</select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="report()">导出</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">修改</a>

        </div>
	<table id="dg"></table>
	<div id="dlg"></div>
</div>
<script type="text/javascript">
var dg;
var d;
//document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	gridDG();
});
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
//申请单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/baseAccountInfo/json',
	    fit : true,
		fitColumns : false,
		border : false,
		//idField : 'forId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
		columns: [[
			{field:'id',title:'id',sortable:false,hidden:true},
			{field: 'billNo', title: '提单号',width: 150},
			{field: 'containerNo', title: '箱号',  width: 150},

			{field: 'productType', title: '货类', width: 100,
				formatter:function (value, row, index){
					if (value == "1"){
						return '水产';
					}else if (value == "2"){
						return '肉类';
					}else {
						return '其他';
					}
				}
			},
			{
				field: 'productName', title: '货物名称(品名)', width: 150},
			{
				field: 'num', title: '件数', width: 50
			},
			{field: 'weight', title: '重量',  width: 100},
			{field: 'stockType', title: '库存类型', width: 100,
				formatter:function (value, row, index) {
					if (value == "0") {
						return '入库';
					} else if (value == "1") {
						return '出库';
					}
				}
			},
			{field: 'appointDate', title: '入库日期',  width: 150},
			{field: 'status', title: '状态', width: 100,
				formatter:function (val,row,index) {
					if (val=="0"){
						return "已保存";
					}else if (val =="1"){
						return "已入闸";
					}else if (val =="2") {
						return "已出闸";
					}else if (val =="3") {
						return "已取消";
					}
				}
			},
		]],
	    onClickRow:function(rowIndex, rowData){

	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

//修改
function upd(){
	var row = dg.datagrid('getSelected');
	console.log('row=========',row)
	if(rowIsNull(row)) return;
	var href='${ctx}/supervision/baseAccountInfo/update/'+row.id;
	add("底账明细修改",href);
}

//添加
function add(title,href){
	if(typeof(href) =='undefined'){
		title="product添加";
		href='${ctx}/supervision/baseAccountInfo/create';
	}
	d=$("#dlg").dialog({
		title: title,
		width: 450,
		height:520,
		href:href,
		maximizable:true,
		modal:true,
		buttons:[{
			text:'确认',
			handler:function(){
				if($("#isupdateproduct").val()==2){
					parent.$.messager.confirm('提示', '若单毛、单重、品名发生改变，则其余数据中包含此product的也将发生变化，是否确认修改？', function(data){
						if (data){
							$("#mainform").submit();
						}
					});
				}else{
					$("#mainform").submit();
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



//导出底账明细
function report() {
	var url = "${ctx}/supervision/baseAccountInfo/export";
	$("#searchFrom").attr("action",url).submit();
}
</script>
</body>
</html>