<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<%--<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="infoForm" method="post">
        </form>


    </div>
</div>

<table id="dg"></table>--%>

<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="月台操作记录" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">

            <input type="text" name="filter_LIKES_platformNo" class="easyui-validatebox"
                   data-options="width:150,prompt: '月台号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>

            <input type="text" name="filter_LIKES_platNo" class="easyui-validatebox"
                   data-options="width:150,prompt: '车号'"/>


            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_warehouseNo" class="easyui-validatebox"
                   data-options="width:150,prompt: '库号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>


            <input type="text" name="filter_GED_arriveTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '车辆到台时间（开始）'"/>
            - <input type="text" name="filter_LED_arriveTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '车辆到台时间（结束）'"/>


            <span class="toolbar-item dialog-tool-separator"></span>
            <select id="status" name="filter_EQS_inoutBoundFlag" class="easyui-combobox"
                    data-options="width:150,prompt: '出/入库'">
                <option></option>
                <option value="1">入库</option>
                <option value="2">出库</option>
            </select>

            <span class="toolbar-item dialog-tool-separator"></span>
            <select id="autoManualFlag" name="filter_EQS_autoManualFlag" class="easyui-combobox"
                    data-options="width:150,prompt: '自动/手动分配'">
                <option></option>
                <option value="1">自动</option>
                <option value="2">手动</option>
            </select>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
            <span class="toolbar-item dialog-tool-separator"></span>

        </form>
        <form id="searchFrom3" action="">
        </form>

    </div>
    <table id="dg"></table>
</div>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };

    $(function () {
        //客户
        $('#searchStockIn').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/platform/station/operationRecord/search',
            fit: true,
            fitColumns: false,
            border: false,
            idField: 'linkId',
            sortOrder: 'desc',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'platformNo', title: '月台号', width: 70},
                {field: 'platformName', title: '月台名称', width: 60},
                {field: 'yyid', title: '预约id', width: 70},
                {
                    field: 'inoutBoundFlag', title: '出/入库标志', width: 80,
                    formatter: function (val, row, index) {
                        if (val=="1"){
                            return '入库';
                        }else if(val=="2") {
                            return '出库';
                        }
                    }
                },
                {
                    field: 'autoManualFlag', title: '自动/手动分配', width: 90,
                    formatter: function (val, row, index) {
                        if (val=="1"){
                            return '自动';
                        }else if(val=="2") {
                            return '手动';
                        }
                    }
                },
                {field: 'arriveTime', title: '车辆到台时间', width: 130},
                {field: 'startTime', title: '开始时间', width: 130},
                {
                    field: 'endTime', title: '结束时间', width: 130
                },
                {
                    field: 'leaveTime', title: '车辆离台时间', width: 130
                },
                {
                    field: 'warehouseNo', title: '库号', width: 60
                },
                {field: 'platNo', title: '车号', width: 80},
                {field: 'containerNo', title: '箱号', width: 70}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //创建查询对象并查询
    function cx() {
        dg.datagrid('clearSelections');
        var obj = $("#searchFrom").serializeObject();

        dg.datagrid('load', obj);
    }
</script>
</body>
</html>