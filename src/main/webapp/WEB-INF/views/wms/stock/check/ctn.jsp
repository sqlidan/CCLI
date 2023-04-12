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
            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号(按英文逗号分隔)'"/>

            <input type="text" name="billCode" class="easyui-validatebox" data-options="width:120,prompt: '提单号'"/>

            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>

            <select id="clientName" name="clientName" class="easyui-validatebox"
                    data-options="width:150,prompt: '客户名称'">
            </select>

            <input type="text" name="floorNum" class="easyui-validatebox" data-options="width:120,prompt: '楼层号'"/>

            <input type="text" name="roomNum" class="easyui-validatebox" data-options="width:120,prompt: '房间号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

            </br>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="exportExcel()">导出报表</a>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               onclick="feedback()">反馈</a>

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
        dg.datagrid('load', $("#searchFrom").serializeObject());
    }

    function exportExcel() {
        $("#searchFrom").attr("action", "${ctx}/stock/check/ctn/excel").submit();
    }

    function feedback() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        d = $("#dlg").dialog({
            title: '反馈',
            width: 380,
            height: 380,
            href: '${ctx}/stock/check/feedback?action=ctnNum&ctnNum=' + row.ctnNum,
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
            url: '${ctx}/stock/check/ctn/search',
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
                {field: 'ctnNum', title: '箱号', sortable: true, width: 15},
                {field: 'billCode', title: '提单号', sortable: true, width: 25},
                {field: 'sku', title: 'sku号', sortable: true, width: 25},
                {field: 'clientName', title: '客户名', sortable: true, width: 25},
                {field: 'cargoName', title: '产品名称', sortable: true, width: 20},
                {field: 'cargoType', title: '产品规格', sortable: true, width: 10},
                {field: 'allpiece', title: '总数量', sortable: true, width: 10},
                {field: 'locationCode', title: '区位', sortable: true, width: 10},
//                {field: 'allnet', title: '总净重', sortable: true, width: 10},
//                {field: 'allgross', title: '总毛重', sortable: true, width: 10},
                {
                    field: 'units', title: '单位', sortable: true, width: 5,
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