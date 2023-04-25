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
        <form id="searchFrom" action="" method="post">
            <input type="text" name="trayCode" class="easyui-validatebox"
                   data-options="width:200,prompt: '托盘号(按英文逗号分隔)'"/>

            <input type="text" name="billCode" class="easyui-validatebox" data-options="width:120,prompt: '提单号'"/>

            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>

            <select id="clientName" name="clientName" class="easyui-validatebox"
                    data-options="width:150,prompt: '客户名称'">
            </select>

            <input type="text" name="floorNum" class="easyui-validatebox" data-options="width:120,prompt: '楼层号'"/>

            <input type="text" name="roomNum" class="easyui-validatebox" data-options="width:120,prompt: '房间号'"/>

            <%--<input type="text" name="asn" class="easyui-validatebox" data-options="width:120,prompt: 'ASN'"/>--%>
            <%--<input type="text" name="sku" class="easyui-validatebox" data-options="width:120,prompt: 'SKU'"/>--%>
            <%--<input type="text" name="contactCode" class="easyui-validatebox" data-options="width:120,prompt: '联系单号'"/>--%>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

            </br>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="exportExcel()">导出报表</a>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               onclick="feedback()">反馈</a>

            <span class="toolbar-item dialog-tool-separator"></span>

        </form>
    </div>

</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">

    var dg;

    //    document.onkeydown = function () {
    //        if (event.keyCode == 13) cx();
    //    };

    //创建查询对象并查询
    function cx() {

        var obj = $("#searchFrom").serializeObject();

        dg.datagrid('load', obj);
    }

    function exportExcel() {
        $("#searchFrom").attr("action", "${ctx}/stock/check/tray/excel").submit();
    }

    function feedback() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        d = $("#dlg").dialog({
            title: '反馈',
            width: 380,
            height: 380,
            href: '${ctx}/stock/check/feedback?action=trayId&trayId=' + row.trayCode,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform").submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });


    }

    $(function () {

        $('#clientName').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'clientName',
            textField: 'clientName',
            mode: 'remote'
        });

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/stock/check/tray/search',
            fit: true,
            fitColumns: true,
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
                {field: 'billCode', title: '提单号', sortable: true, width: 130},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 130},
//                {field: 'asn', title: 'ASN', sortable: true, width: 90},
                {field: 'sku', title: 'SKU', sortable: true, width: 120},
//                {field: 'contactCode', title: '联系单号', sortable: true, width: 135},
                {field: 'clientName', title: '客户名称', sortable: true, width: 150},
//                {field: 'warehouse', title: '仓库名', sortable: true, width: 80},
//                {field: 'locationCode', title: '库位号', sortable: true, width: 80},
                {field: 'cargoName', title: '产品名称', sortable: true, width: 100},
                {field: 'cargoType', title: '产品规格', sortable: true, width: 100},
                {field: 'nowNum', title: '数量', sortable: true, width: 100},
                {field: 'locationCode', title: '区位', sortable: true, width: 100},
//                {field: 'netWeight', title: '净重', sortable: true, width: 100},
//                {field: 'grossWeight', title: '毛重', sortable: true, width: 100},
//                {field: 'allpiece', title: '总数量', hidden: true},
//                {field: 'allnet', title: '总净重', hidden: true},
//                {field: 'allgross', title: '总毛重', hidden: true},
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
                }
            ]],
            onLoadSuccess: function (data) {

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

</script>
</body>
</html>