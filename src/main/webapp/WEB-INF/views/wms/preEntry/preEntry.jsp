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
			<input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
			<select id="searchStock" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'" >
			</select>
			<select name="filter_EQS_serviceProject" class="easyui-combobox" data-options="width:150,prompt: '服务项目' " >
				<option  value=""></option>
				<option  value="0">报进</option>
				<option  value="1">报出</option>
			</select>
			<select name="filter_EQS_state" class="easyui-combobox" data-options="width:150,prompt: '状态' " >
				<option  value=""></option>
				<option  value="0">待完善</option>
				<option  value="1">待初审</option>
				<option  value="2">待复审</option>
				<option  value="3">待申报</option>
				<option  value="4">申报核注清单中</option>
				<option  value="5">核注清单通过</option>
<%--				<option  value="6">报关中</option>--%>
<%--				<option  value="7">报关通过</option>--%>
			</select>
			<input type="text" name="filter_LIKES_declarationUnit" class="easyui-validatebox" data-options="width:150,prompt: '报关公司'"/>
			<input type="text" name="filter_GED_createTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '创建开始日期'"/>
			<input type="text" name="filter_LED_createTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '创建结束日期'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="wms:preEntry:add">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="add()">添加</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:edit">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:delete">
			 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:add">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="ck()">查看预报单</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:jlExamineOk">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="jlUpdateOk()">初审通过</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:jlExamineNo">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="jlUpdateNo()">初审驳回</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:zgExamineOk">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="zgUpdateOk()">复审通过</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:zgExamineNo">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="zgUpdateNo()">复审驳回</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:uploadBGD">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="uploadBGD()">上传报关单</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="wms:preEntry:downloadBGD">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="downloadBGD()">下载报关单</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
	</div>
</div>

<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
var dg;
var d;
var dt;

$(function(){   
	ajaxS();	
	gridDG();
});

// =================================================================================
document.onkeydown = function () {if(event.keyCode == 13){cx();}};
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj);
}

//客户名称下拉
function ajaxS(){
	//客户
	$('#searchStock').combobox({
		method:"GET",
		url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
		valueField: 'clientName',
		textField: 'clientName',
		mode:'remote'
	});
}

//预报单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/preEntry/json',
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
		pageList : [ 20, 50, 100, 200, 500 ],
		singleSelect:true,
		frozenColumns: [[
			{field:'forId',title:'预报单ID',sortable:true},
			{field:'serviceProject',title:'服务项目',sortable:true,
				formatter : function(value, row, index) {
					if(value == '0'){
						return "报进";
					}
					if(value == '1'){
						return "报出";
					}
				}},
			{field:'state',title:'状态',sortable:true,
				formatter : function(value, row, index) {
					if(value == '0'){
						return "待完善";
					}
					if(value == '1'){
						return "待初审";
					}
					if(value == '2'){
						return "待复审";
					}
					if(value == '3'){
						return "待申报";
					}
					if(value == '4'){
						return "申报核注清单中";
					}
					if(value == '5'){
						return "核注清单通过";
					}
					if(value == '6'){
						return "报关中";
					}
					if(value == '7'){
						return "报关通过";
					}
				}},
		]],
	    columns:[[
			{field:'jlAudit',title:'初审人',sortable:true},
			{field:'jlAuditTime',title:'初审时间',sortable:true,
				formatter : function(value, row, index) {
					if(value !== undefined && value!== null && value.toString().length >= 10 ){
						return value.toString().substring(0,10);
					}else{
						return '';
					}
				}},
			{field:'jlRejectReason',title:'初审驳回原因',sortable:true},
			{field:'zgAudit',title:'复审人',sortable:true},
			{field:'zgAuditTime',title:'复审时间',sortable:true,
				formatter : function(value, row, index) {
					if(value !== undefined && value!== null && value.toString().length >= 10 ){
						return value.toString().substring(0,10);
					}else{
						return '';
					}
				}},
			{field:'zgRejectReason',title:'复审驳回原因',sortable:true},
 	        // {field:'clientName',title:'客户名称',sortable:true},
			{field:'declarationUnit',title:'报关公司',sortable:true},
			// {field:'tradeMode',title:'贸易方式',sortable:true},
			{field:'billNum',title:'提单号',sortable:true},
			// {field:'ctnCont',title:'箱量',sortable:true},
			{field:'productName',title:'品名',sortable:true},
			{field:'hsNo',title:'HS编码',sortable:true},
			{field:'price',title:'重量',sortable:true},//件数
			// {field:'netWeight',title:'重量',sortable:true},
			{field:'consignee',title:'收货人',sortable:true},
			{field:'consignor',title:'发货人',sortable:true},
			{field:'contryOragin',title:'原产国',sortable:true},
			// {field:'seqNo',title:'通关一点通编号',sortable:true},
			{field:'checkListNo',title:'核注清单号',sortable:true},
			{field:'cdNum',title:'报关单号',sortable:true},
			// {field:'remark',title:'备注',sortable:true},
			{field:'cdBy',title:'报关人',sortable:true},
			{field:'cdTime',title:'报关时间',sortable:true},
			// {field:'upAndDown',title:'是否上传报关单',sortable:true,
			// 	formatter : function(value, row, index) {
			// 		if(value == '0'){
			// 			return "未上传";
			// 		}
			// 		if(value == '1'){
			// 			return "已上传";
			// 		}
			// 		if(value == '2'){
			// 			return "已下载";
			// 		}
			// 	}},
			// {field:'upFileName',title:'文件名称',sortable:true},
			// {field:'upBy',title:'上传人',sortable:true},
			// {field:'upTime',title:'上传时间',sortable:true},
			// {field:'upAndDown',title:'上传/下载',sortable:true,
			// 	formatter : function(value, row, index) {
			// 		if(value == '0'){
			// 			return "未上传";
			// 		}
			// 		if(value == '1'){
			// 			return "已上传";
			// 		}
			// 		if(value == '2'){
			// 			return "已下载";
			// 		}
			// 	}},
			{field:'createBy',title:'创建人',sortable:true},
			{field:'createTime',title:'创建时间',sortable:true,
				formatter : function(value, row, index) {
					if(value !== undefined && value!== null && value.toString().length >= 10 ){
						return value.toString().substring(0,10);
					}else{
						return '';
					}
				}},
			// {field:'updateBy',title:'修改人',sortable:true},
			// {field:'updateTime',title:'修改时间',sortable:true},
	    ]],
	    // onClickRow:function(rowIndex, rowData){
	    // 	info(rowData.forId);
	    // },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}
//添加
function add(){
	window.parent.mainpage.mainTabs.addModule('预报单添加','wms/preEntry/manager');
}
//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('预报单修改','wms/preEntry/update/' + row.forId);
}
//查看
function ck(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('预报单查看','wms/preEntry/updateCK/' + row.forId);
}
//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条预报单数据！", position: "bottomRight" });
		return;
	}
	if(parseInt(row.state) > 0){
		parent.$.messager.show({ title : "提示",msg: "预报单信息已完善提交，不可删除。", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntry/delete/"+row.forId,
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}
//初审通过
function jlUpdateOk(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条预报单数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '您确定要将选中的预报单信息审核通过？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntry/UpdateState/"+row.forId+"/jlUpdateOk",
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}
//初审驳回
function jlUpdateNo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条预报单数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.prompt('提示', '驳回原因：', function(content){
		if (content){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntry/UpdateState/"+row.forId+"/jlUpdateNo?reason="+content,
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}
//复审通过
function zgUpdateOk(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条预报单数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '您确定要将选中的预报单信息审核通过？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntry/UpdateState/"+row.forId+"/zgUpdateOk",
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}
//复审驳回
function zgUpdateNo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条预报单数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.prompt('提示', '驳回原因：', function(content){
		if (content){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntry/UpdateState/"+row.forId+"/zgUpdateNo?reason="+content,
				success: function(data){
					successTip(data,dg);
				},
			});
		}
	});
}

// ==============================上传单个文件===================================================
function uploadBGD(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条预报单数据！", position: "bottomRight" });
		return;
	}

	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	dt=$("#dlg").dialog({
		title: '上传报关单',
		width: 500,
		height: 300,
		href:'${ctx}/wms/preEntry/fileUploadH/'+row.forId,
		maximizable:true,
		modal:true,
		buttons:[{
			text:'取消',
			handler:function(){
				dt.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){gridDG(row.forId)},100);
		}
	});
}
// ===========================下载单个文件======================================================
function downloadBGD(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)){
		parent.$.messager.show({ title : "提示",msg: "请选择一条预报单数据！", position: "bottomRight" });
		return;
	}

	var requestUrl = "${ctx}/wms/preEntry/fileDownload?fileName="+row.upFileName+"&forId="+row.forId;
	var xhr = new XMLHttpRequest();
	//GET请求,请求路径url,async(是否异步)
	xhr.open('GET', requestUrl, true);
	//设置响应类型为 blob
	xhr.responseType = 'blob';
	//关键部分
	xhr.onload = function (e) {
		//如果请求执行成功
		if (this.status == 200) {
			var blob = this.response;
			var a = document.createElement('a');

			blob.type = "application/octet-stream";
			//创键临时url对象
			var url = URL.createObjectURL(blob);

			a.href = url;
			a.download=row.upFileName;
			a.click();
			//释放之前创建的URL对象
			window.URL.revokeObjectURL(url);
		}
	};
	//发送请求
	xhr.send();
}
</script>
</body>
</html>