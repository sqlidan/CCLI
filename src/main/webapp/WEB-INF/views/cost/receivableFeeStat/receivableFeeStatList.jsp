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
        <select class="easyui-combobox" id="customerName" name="filter_EQS_customerName" data-options="width:180,prompt:'客户名称'">
            <option value=""></option>
        </select>
        <input id="accountPeriod" name="filter_EQS_accountPeriod" type="text" class="easyui-my97" datefmt="yyyy-MM" data-options="width:120,prompt:'账期'"/>
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
        fitColumns: true,
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
                        return '0.00';
                    }
                    return Number(value).toFixed(2);
                }
            }
        ]],
        enableHeaderClickMenu: true,
        enableHeaderContextMenu: true,
        enableRowContextMenu: false,
        toolbar:'#tb'
    });

    $('#customerName').combobox({
        method: 'GET',
        url: '${ctx}/base/client/getClientAll?tim=1',
        valueField: 'clientName',
        textField: 'clientName',
        mode: 'remote'
    });
});

function cx(){
    var obj = $("#searchFrom").serializeObject();
    dg.datagrid('load', obj);
}

function exportExcel() {
    var statDateStart = $("input[name='filter_GED_statDate']").val();
    var statDateEnd = $("input[name='filter_LED_statDate']").val();
    if (statDateStart == '' || statDateEnd == '') {
        parent.$.easyui.messager.alert("请选择统计日期开始和结束后再导出！");
        return;
    }
    var url = "${ctx}/cost/receivableFeeStat/export";
    var queryString = $("#searchFrom").serialize();
    window.location.href = url + (queryString == '' ? '' : '?' + queryString);
}
</script>
</body>
</html>
