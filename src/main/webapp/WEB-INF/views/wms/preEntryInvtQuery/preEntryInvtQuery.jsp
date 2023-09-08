<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="核注清单查询">
		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_bondInvtNo" class="easyui-validatebox" data-options="width:150,prompt: '核注清单号'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
		</form>
		<form id="searchFrom3" action="">
		</form>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" data-options="disabled:false" onclick="queryHZQD()">查询核注清单</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="ck()">查看核注清单详情</a>
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
	    url:'${ctx}/wms/preEntryInvtQuery/json',
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
		pageList : [20, 30, 50, 100 ],
		singleSelect:true,
		frozenColumns: [[
			{field: 'id', title: 'id',sortable:true},
			{field: 'bondInvtNo', title: '核注清单号',sortable:true},
		]],
	    columns:[[
	        {field:'bizopEtpsNM',title:'经营企业名称',sortable:true},
			{field:'bondInvtType',title:'清单类型',sortable:true},
			{field:'dclcusFlag',title:'是否报关',sortable:true,
				formatter : function(value, row, index) {
					if(value == '1'){
						return "报关";
					}
					if(value == '2'){
						return "非报关";
					}
				}},
			{field:'dclcusTypeCD',title:'报关类型代码',sortable:true},
			{field:'invtDclTime',title:'清单申报时间',sortable:true},
			{field:'listStat',title:'清单状态',sortable:true},
			{field:'putrecNo',title:'账册号',sortable:true},
			{field:'rcvgdEtpsNM',title:'收货企业名称',sortable:true},
			{field:'seqNo',title:'预录入统一编号',sortable:true},
			{field:'supvModeCD',title:'监管方式代码',sortable:true},
			{field:'vrfdedMarkcd',title:'核扣标记',sortable:true,
				formatter : function(value, row, index) {
					if(value == '0'){
						return "未核扣";
					}
					if(value == '1'){
						return "预核扣";
					}
					if(value == '2'){
						return "已核扣";
					}
					if(value == '3'){
						return "已核销";
					}
				}},
			{field:'entryNo',title:'报关单号',sortable:true},
			{field:'putrecSeqno',title:'备案编号',sortable:true},
			{field:'dclTypecd',title:'申报类型',sortable:true},
			{field:'invtStucd',title:'清单审批状态',sortable:true},
			{field:'singleNo',title:'单一窗口编号',sortable:true},
			{field:'impexpMarkcd',title:'进出口标记',sortable:true,
				formatter : function(value, row, index) {
					if(value == 'I'){
						return "进口";
					}
					if(value == 'E'){
						return "出口";
					}
				}},
			{field:'mtpckEndprdMarkcd',title:'料件成品标记',sortable:true,
				formatter : function(value, row, index) {
					if(value == 'I'){
						return "料件";
					}
					if(value == 'E'){
						return "成品";
					}
				}},
			{field:'trspModecd',title:'运输方式',sortable:true},
			{field:'gdsSeqno',title:'商品序号',sortable:true},
			{field:'dclQty',title:'申报数量',sortable:true},
			{field:'dclUnitcd',title:'申报单位',sortable:true},
			{field:'gdsMtno',title:'商品料号',sortable:true},
			{field:'bizopEtpsno',title:'经营企业编号',sortable:true},
			{field:'dclEtpsNm',title:'申报企业名称',sortable:true},
			{field:'etpsInnerInvtNo',title:'企业内部编号',sortable:true},
			{field:'createBy',title:'创建人',sortable:true},
			{field:'createTime',title:'创建日期',sortable:true},
			{field:'updateBy',title:'修改人',sortable:true},
			{field:'updateTime',title:'修改时间',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

//搜索
function cx(){
	dg.datagrid('clearSelections');
	var obj=$("#searchFrom").serializeObject();

	dg.datagrid('load',obj); 
}
//查询核注清单信息
function queryHZQD(){
	parent.$.messager.prompt('提示', '请输入需要查询的核注清单号。', function(content){
		if (content){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/preEntryInvtQuery/getPreEntryInvtQuery/"+content,
				success: function(data){
					successTip(data,dg);
				},
			});
		}else{
			parent.$.messager.show({ title : "提示",msg: "请输入核注清单号！", position: "bottomRight" });
			return;
		}
	});
}
//查看
function ck(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('核注清单详情','wms/preEntryInvtQuery/invtDetail/' + row.id);
}
</script>
</body>
</html>