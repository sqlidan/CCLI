<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFrom" action="">

            <select id="searchStock" name="clientName" class="easyui-combobox"
                    data-options="width:150,prompt: '客户名称'">
            </select>

            <input type="text" name="billCode" class="easyui-validatebox"
                   data-options="width:150,prompt: '提单号'"/>

            <input type="text" name="ctnNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '箱号'"/>

            <!--  <input type="text" name="filter_EQS_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>-->
            <input id="backupTime" name="backupTime" type="text" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width: 150,prompt:'日期', required:'required'"
                   value="<fmt:formatDate value="${stock.backupTime}"/>"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>

        <shiro:hasPermission name="report:inStock:common">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="exportReportExcel()">导出EXCEL</a>
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
    var d, dt;
    var splitJson, tids;//用于记录需要拆分的托盘记录

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };

    $(document).ready(function () {

        // 下来匡获取客户信息
        $('#searchStock').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'clientName',
            textField: 'clientName',
            mode: 'remote'
        });

    });

    function dgcx(obj) {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/report/history/json',
            queryParams: obj,
            fit: true,
            fitColumns: false,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'trayCode', title: '托盘号', sortable: true, width: 100},
                {field: 'billCode', title: '提单号', sortable: true, width: 100},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 100},
                {field: 'asn', title: 'ASN', sortable: true, width: 100},
                {field: 'sku', title: 'SKU', sortable: true, width: 100},
                {field: 'contactCode', title: '联系单号', sortable: true, width: 100},
                {field: 'clientName', title: '客户名称', sortable: true, width: 100},
                {field: 'warehouse', title: '仓库名', sortable: true, width: 100},
                {field: 'locationCode', title: '库位号', sortable: true, width: 100},
                {field: 'cargoName', title: '产品名称', sortable: true, width: 100},
                {field: 'nowNum', title: '现有数量', sortable: true, width: 100},
                {field: 'netWeight', title: '净重', sortable: true, width: 100},
                {field: 'grossWeight', title: '毛重', sortable: true, width: 100},
                {
                    field: 'units', title: '单位', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '1') {
                            return '千克';
                        }
                        if (value == '2') {
                            return '吨';
                        }
                    }
                },
                {
                    field: 'state', title: '状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '00') {
                            return '已收货';
                        }
                        if (value == '01') {
                            return '已上架';
                        }
                        if (value == '10') {
                            return '出库中';
                        }
                        if (value == '11') {
                            return '出库理货';
                        }
                        if (value == '12') {
                            return '已出库';
                        }
                        if (value == '20') {
                            return '待回库';
                        }
                        if (value == '21') {
                            return '回库收货';
                        }
                        if (value == '99') {
                            return '货损';
                        }
                    }
                },
                {field: 'enterTime', title: '入库理货时间', sortable: true, width: 100},
                {field: 'enterPerson', title: '入库理货员', sortable: true, width: 100},
                {field: 'enterOp', title: '入库操作员', sortable: true, width: 100},
                {
                    field: 'backupTime', title: '备份日期', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        date = new Date(value);
                        return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                    }
                }
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    }

    //查询
    function cx() {

//        if ($("#backupTime").datebox("getValue") == "") {
        if (!$("#searchFrom").form('validate')) {
            parent.$.messager.show({title: "提示", msg: "日期不能为空！", position: "bottomRight"});
            return;
        }

        var obj = $("#searchFrom").serializeObject();

        dgcx(obj);
    }

    // 导出Excel表格
    function exportReportExcel() {

        if ($("#searchFrom").form('validate')) {

            $("#searchFrom").attr("action", "${ctx}/report/history/exportReportExcel").submit();

        } else {
            parent.$.messager.show({title: "提示", msg: "日期不能为空！", position: "bottomRight"});
        }

    }

</script>
</body>
</html>