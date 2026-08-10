<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <form id="searchForm" action="">
        <input type="text" name="schemeNum" class="easyui-validatebox" data-options="width:150,prompt:'方案编号'"/>
        <input type="text" name="schemeName" class="easyui-validatebox" data-options="width:150,prompt:'方案名称'"/>
        <input type="text" name="contractNum" class="easyui-validatebox" data-options="width:150,prompt:'合同号'"/>
        <input id="customerName" name="customerName" class="easyui-validatebox" data-options="width:150,prompt:'客户名称'"/>
        <select class="easyui-combobox" name="ifGet" data-options="width:120,prompt:'收付类型'">
            <option value="">收付类型</option>
            <option value="1">应收</option>
            <option value="2">应付</option>
        </select>
        <select class="easyui-combobox" name="programState" data-options="width:120,prompt:'方案状态'">
            <option value="">方案状态</option>
            <option value="1">已审核</option>
            <option value="0">未审核</option>
        </select>
        <select class="easyui-combobox" name="contractState" data-options="width:120,prompt:'合同状态'">
            <option value="">合同状态</option>
            <option value="1">已审核</option>
            <option value="0">未审核</option>
        </select>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchData()">查询</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-zoom" plain="true" onclick="showDetail()">查看</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">Excel导出</a>
    </form>
</div>
<table id="dg"></table>
<script type="text/javascript">
var dg;

$(function(){
    $('#customerName').combobox({
        method:'GET',
        url:'${ctx}/base/client/getClientAll',
        valueField:'clientName',
        textField:'clientName',
        mode:'remote'
    });

    dg = $('#dg').datagrid({
        method:'GET',
        url:'${ctx}/base/customerExpenseScheme/json',
        fit:true,
        fitColumns:true,
        border:false,
        idField:'schemeNum',
        striped:true,
        pagination:true,
        rownumbers:true,
        pageNumber:1,
        pageSize:20,
        pageList:[10,20,30,40,50],
        singleSelect:true,
        columns:[[
            {field:'schemeNum',title:'方案编号',sortable:true,width:110},
            {field:'schemeName',title:'方案名称',sortable:true,width:130},
            {field:'customsName',title:'方案客户',sortable:true,width:130},
            {field:'contractNum',title:'合同号',sortable:true,width:120},
            {field:'contractClientName',title:'合同客户',sortable:true,width:130},
            {field:'contractState',title:'合同状态',sortable:true,width:80,formatter:formatState},
            {field:'signTime',title:'签订时间',sortable:true,width:100},
            {field:'expirationTime',title:'到期时间',sortable:true,width:100},
            {field:'canvassionPerson',title:'揽货人',sortable:true,width:90},
            {field:'ifGet',title:'收付类型',sortable:true,width:80,formatter:formatIfGet},
            {field:'programState',title:'方案状态',sortable:true,width:80,formatter:formatState},
            {field:'operatorPerson',title:'创建人',sortable:true,width:90},
            {field:'operateTime',title:'创建时间',sortable:true,width:145},
            {field:'schemeRemark',title:'方案备注',sortable:true,width:160},
            {field:'contractRemark',title:'合同备注',sortable:true,width:160}
        ]],
        toolbar:'#tb'
    });
});

$(document).keypress(function(e){
    if(e.which == 13){
        searchData();
    }
});

function formatState(value){
    return value == '1' ? '已审核' : '未审核';
}

function formatIfGet(value){
    if(value == '1'){
        return '应收';
    }
    if(value == '2'){
        return '应付';
    }
    return '';
}

function searchData(){
    dg.datagrid('load', $('#searchForm').serializeObject());
}

function showDetail(){
    var row = dg.datagrid('getSelected');
    if(rowIsNull(row)){
        return;
    }
    window.parent.mainpage.mainTabs.addModule('客户费用方案详情', 'base/customerExpenseScheme/detail/' + row.schemeNum);
}

function exportExcel(){
    $('#searchForm').attr('action', '${ctx}/base/customerExpenseScheme/export').submit();
}
</script>
</body>
</html>
