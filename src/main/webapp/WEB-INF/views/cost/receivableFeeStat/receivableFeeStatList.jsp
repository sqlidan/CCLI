<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <form id="searchFrom" action="">
        <input type="text" name="filter_LIKES_customerName" class="easyui-validatebox" data-options="width:160,prompt:'客户名称'"/>
        <input type="text" name="filter_EQS_accountPeriod" class="easyui-validatebox" data-options="width:120,prompt:'账期 yyyy-MM'"/>
        <input type="text" name="filter_LIKES_feeName" class="easyui-validatebox" data-options="width:150,prompt:'费目'"/>
        <input type="text" name="filter_GED_statDate" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt:'统计日期开始'"/>
        -
        <input type="text" name="filter_LED_statDate" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt:'统计日期结束'"/>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出</a>
    </form>
</div>

<table id="dg"></table>

<script type="text/javascript">
var dg;

document.onkeydown = function () { if (event.keyCode == 13) { cx(); } };

$(function(){
    dg = $('#dg').datagrid({
        method: "get",
        url: '${ctx}/cost/receivableFeeStat/json',
        fit: true,
        fitColumns: false,
        border: false,
        sortName: 'statDate',
        sortOrder: 'desc',
        striped: true,
        pagination: true,
        rownumbers: true,
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 40, 50],
        singleSelect: true,
        columns:[[
            {field:'id', title:'ID', hidden:true},
            {field:'customerName', title:'客户名称', sortable:true, width:220},
            {field:'accountPeriod', title:'账期', sortable:true, width:100},
            {field:'statDate', title:'统计日期', sortable:true, width:120},
            {field:'feeCode', title:'费目代码', sortable:true, width:100},
            {field:'feeName', title:'费目', sortable:true, width:180},
            {field:'amount', title:'金额', sortable:true, width:120, align:'right',
                formatter:function(value) {
                    if (value == null || value === '') {
                        return '0.0000';
                    }
                    return Number(value).toFixed(4);
                }
            }
        ]],
        enableHeaderClickMenu: true,
        enableHeaderContextMenu: true,
        enableRowContextMenu: false,
        toolbar:'#tb'
    });
});

function cx(){
    var obj = $("#searchFrom").serializeObject();
    dg.datagrid('load', obj);
}

function exportExcel() {
    var url = "${ctx}/cost/receivableFeeStat/export";
    $("#searchFrom").attr("action", url).submit();
}
</script>
</body>
</html>
