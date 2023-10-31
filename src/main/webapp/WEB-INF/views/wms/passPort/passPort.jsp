<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
	<div>
		<form id="searchFrom" action="">
			<select name="filter_EQS_ioTypecd" class="easyui-combobox" data-options="width:150,prompt: '进出标志' " >
				<option value=""></option>
				<option value="I">进区</option>
				<option value="E">出区</option>
			</select>
			<select name="filter_EQS_state" class="easyui-combobox" data-options="width:150,prompt: '数据状态' " >
				<option value=""></option>
				<option value="0">新增</option>
				<option value="1">申报</option>
				<option value="2">通过</option>
				<option value="3">作废</option>
				<option value="4">转人工</option>
				<option value="5">退单</option>
				<option value="Y">入库成功</option>
				<option value="Z">入库失败</option>
			</select>
			<select name="filter_EQS_bindTypecd" class="easyui-combobox" data-options="width:150,prompt: '绑定类型' " >
				<option value=""></option>
				<option value="1">一车多票</option>
				<option value="2">一车一票</option>
				<option value="3">一票多车</option>
			</select>
      	    <select id="passportTypecd" name="filter_LIKES_passportTypecd" class="easyui-combobox" data-options="width:150,prompt: '核放单类型'" >
				<option value=""></option>
				<option value="1">先入区后报关</option>
				<option value="2">一线一体化进出区</option>
				<option value="3">二线进出区</option>
				<option value="4">非报关进出区</option>
				<option value="5">卡口登记货物</option>
				<option value="6">空车进出区</option>
				<option value="7">两步申报</option>
      	    </select>
			<select id="rltTbTypecd" name="filter_LIKES_rltTbTypecd" class="easyui-combobox" data-options="width:150,prompt: '关联单证类型'" >
				<option value=""></option>
				<option value="1">核注清单</option>
				<option value="2">出入库单</option>
				<option value="3">提运单</option>
			</select>
			<input type="text" name="filter_LIKES_rltNo" class="easyui-validatebox" data-options="width:150,prompt: '关联单证编号'"/>
			<input type="text" name="filter_LIKES_seqNo" class="easyui-validatebox" data-options="width:150,prompt: '预录入统一编号'"/>
			<input type="text" name="filter_LIKES_passportNo" class="easyui-validatebox" data-options="width:150,prompt: '核放单编号'"/>
			<input type="text" name="filter_LIKES_etpsPreentNo" class="easyui-validatebox" data-options="width:150,prompt: '企业内部编号'"/>
			<input type="text" name="filter_LIKES_areainEtpsno" class="easyui-validatebox" data-options="width:150,prompt: '区内企业编码'"/>
			<input type="text" name="filter_LIKES_vehicleNo" class="easyui-validatebox" data-options="width:150,prompt: '承运车车牌号'"/>
			<select id="dclTypecd" name="filter_LIKES_dclTypecd" class="easyui-combobox" data-options="width:150,prompt: '申报类型'" >
				<option value=""></option>
				<option value="1">备案</option>
				<option value="2">变更</option>
				<option value="3">删除</option>
			</select>
			<select name="filter_EQS_lockage" class="easyui-combobox" data-options="width:150,prompt: '过卡状态' " >
				<option value=""></option>
				<option value="0">已申请</option>
				<option value="1">已审批(卡口放行)</option>
				<option value="2">已过卡</option>
				<option value="3">已过一卡</option>
				<option value="4">已过二卡</option>
				<option value="5">已删除</option>
				<option value="6">已作废(拒绝过卡)</option>
			</select>
	        <input type="text" name="filter_GED_createTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '录入开始日期'"/>
	        <input type="text" name="filter_LED_createTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '录入结束日期'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>

		<shiro:hasPermission name="wms:passPort:add">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="add()">新增</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
      	<shiro:hasPermission name="wms:passPort:edit">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">编辑</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
		<shiro:hasPermission name="wms:passPort:delete">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="deleteInfo()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:passPort:submit">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="submit()">申报</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:passPort:cancel">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="cancel()">作废</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:passPort:submit">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="synchronization()">同步</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
        </div>
</div>
<table id="dg"></table>
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg
$(function(){   
	gridDG();
});


// =================================================================================
document.onkeydown = function () {if(event.keyCode == 13){cx();}};
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj);
}

//预报单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/passPort/json',
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 20, 50, 100, 200, 500 ],
		singleSelect:true,
	    columns:[[   
	    	{field:'seqNo',title:'预录入统一编号',sortable:true},
	        {field:'passportNo',title:'核放单编号',sortable:true},
 	        {field:'passportTypecd',title:'核放单类型',sortable:true,
				formatter : function(value, row, index) {
					if(value == '1'){
						return "先入区后报关";
					}
					if(value == '2'){
						return "一线一体化进出区";
					}
					if(value == '3'){
						return "二线进出区";
					}
					if(value == '4'){
						return "非报关进出区";
					}
					if(value == '5'){
						return "卡口登记货物";
					}
					if(value == '6'){
						return "空车进出区";
					}
					if(value == '7'){
						return "两步申报";
					}
					return value;
				}},
			{field:'ioTypecd',title:'进出标志',sortable:true,
				formatter : function(value, row, index) {
					if(value == 'I'){
						return "进区";
					}
					if(value == 'E'){
						return "出区";
					}
				}},
			{field:'dclTypecd',title:'申报类型',sortable:true,
				formatter : function(value, row, index) {
					if(value == '1'){
						return "备案";
					}
					if(value == '2'){
						return "变更";
					}
					if(value == '3'){
						return "作废";
					}
					if(value == '5'){
						return "删除";
					}
				}},
			{field:'state',title:'状态',sortable:true,
				formatter : function(value, row, index) {
					if(value == '0'){
						return "新增";
					}
					if(value == '1'){
						return "申报";
					}
					if(value == '2'){
						return "通过";
					}
					if(value == '3'){
						return "作废";
					}
					if(value == '4'){
						return "转人工";
					}
					if(value == '5'){
						return "退单";
					}
					if(value == 'Y'){
						return "入库成功";
					}
					if(value == 'Z'){
						return "入库失败";
					}
				}},
			{field:'areainEtpsNm',title:'区内企业名称',sortable:true},
			{field:'etpsPreentNo',title:'企业内部编号',sortable:true},
			{field:'bindTypecd',title:'绑定类型',sortable:true,
				formatter : function(value, row, index) {
					if(value == '1'){
						return "一车多票";
					}
					if(value == '2'){
						return "一车一票";
					}
					if(value == '3'){
						return "一票多车";
					}
				}},
			{field:'rltNo',title:'关联单证号',sortable:true},
			{field:'dclTime',title:'申报日期',sortable:true},
			{field:'dclEtpsNm',title:'申报单位',sortable:true},
			{field:'dclEtpsno',title:'申报单位编码',sortable:true},
			{field:'vehicleNo',title:'承运车牌号',sortable:true},
			{field:'lockage',title:'是否过卡',sortable:true,
				formatter : function(value, row, index) {
					if(value == '0'){
						return "已申请";
					}
					if(value == '1'){
						return "已审批(卡口放行)";
					}
					if(value == '2'){
						return "已过卡";
					}
					if(value == '3'){
						return "已过一卡";
					}
					if(value == '4'){
						return "已过二卡";
					}
					if(value == '5'){
						return "已删除";
					}
					if(value == '6'){
						return "已作废(拒绝过卡)";
					}
				}},
			{field:'checkResult',title:'查验结果',sortable:true},
			{field:'lockageTime1',title:'过卡时间1',sortable:true},
			{field:'lockageTime2',title:'过卡时间2',sortable:true},
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}
//新增
function add(){
	window.parent.mainpage.mainTabs.addModule('核放单新增','wms/passPort/add');
}
//编辑
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('核放单编辑','wms/passPort/update/' + row.id);
}
//删除
function deleteInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条核放单数据！", position: "bottomRight" });
		return;
	}
	if(parseInt(row.state) > 0){
		parent.$.messager.show({ title : "提示",msg: "当前核放单已申报，不可删除！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/passPort/delete/"+row.id,
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}
//申报
function submit(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条核放单数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '您确定要将选中的核放单信息进行申报吗？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/passPort/UpdateState/"+row.id+"/1",
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}
//作废
function cancel(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条核放单数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '您确定要将选中的核放单信息进行作废吗？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/passPort/UpdateState/"+row.id+"/3",
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}
//同步
function synchronization(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条核放单数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '您确定要将选中的核放单信息进行同步吗？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/passPort/synchronization/"+row.id,
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}

</script>
</body>
</html>