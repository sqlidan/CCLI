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

            <input type="text" id="billNum" name="billNum" class="easyui-validatebox"
                   data-options="width:120,prompt: '提单号'">
            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>

            <input type="hidden" id="clientName" name="clientName"/>
            <select id="client" name="client" class="easyui-validatebox"
                    data-options="width:120,prompt: '客户名'"></select>

            <input type="text" name="carCode" class="easyui-validatebox" data-options="width:120,prompt: '车牌号'"/>
            <select id="planTime" name="planTime" class="easyui-validatebox"
                    data-options="width:120,prompt: '预约时间'"></select>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

        </form>

        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="serviceCheck()">客服审核</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="checkItem()">计划审核</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="rejectCheck()">驳回</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <%--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"--%>
        <%--onclick="setPlanTime()">预约</a>--%>
    </div>

</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">

    var dg;
    var d;

    document.onkeydown = function () {
        if (event.keyCode == 13) cx();
    };

    //创建查询对象并查询
    function cx() {

        $("#clientName").val($("#client").combobox("getText"));

        dg.datagrid('load', $("#searchFrom").serializeObject());
    }

    $(function () {

        $('#client').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onLoadSuccess: function () {

            }
        });

        $('#planTime').combobox({
            method: "GET",
            url: "${ctx}/wms/plan/time/select",
            valueField: 'PLANTIME',
            textField: 'PLANTIMETEXT',
            mode: 'remote',
            onLoadSuccess: function () {

            }
        });

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/plan/page',
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
                {
                    field: 'checkState', title: '审核状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '-1') {
                            return '已驳回';
                        }
                        if (value == '0') {
                            return '未审核';
                        }
                        if (value == '1') {
                            return '已客服审核';
                        }
                        if (value == '2') {
                            return '已计划审核';
                        }
                    }
                },
                {field: 'id', title: 'id', hidden: true},
                {field: 'billNum', title: '提单号', sortable: true, width: 100},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 100},
                {field: 'skuId', title: 'SKU', sortable: true, width: 100},
                {field: 'clientName', title: '客户名', sortable: true, width: 100},
                {field: 'loadNum', title: '提货量', sortable: true, width: 100},
                {field: 'quantity', title: '件数', sortable: true, width: 100},
                {field: 'cargoName', title: '产品名', sortable: true, width: 100},
                {
                    field: 'planTime', title: '预约时间', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        return value == null ? "" : value.substring(0, 2) + ":" + value.substring(2, 4);
                    }
                },
                {field: 'planDate', title: '预约日期', sortable: true, width: 100},

                {field: 'carCode', title: '车牌号', sortable: true, width: 100},
//                {field: 'checkDate', title: '审核时间', sortable: true, width: 100},
                {field: 'checkUser', title: '审核人', sortable: true, width: 100}
            ]],
            onLoadSuccess: function (data) {

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

    });

    /**
     * 客服审核
     */
    function serviceCheck() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.checkState != "0" || row.checkState == "-1") {
            toast("预约单不能进行客服审核");
            return;
        }

        var url = '${ctx}/wms/plan/check/service?id=' + row.id;

        $.get(url, function (data) {

            if (data == "success") {
                toast("客服审核成功");
                cx();
            }

        });
    }

    /**
     * 计划审核
     */
    function checkItem() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.checkState != "1" || row.checkState == "-1") {
            toast("预约单不能进行计划审核");
            return;
        }

        var url = '${ctx}/wms/plan/check/plan?id=' + row.id;

        $.get(url, function (data) {

            if (data == "success") {
                toast("计划审核成功");
                cx();
            }

        });

    }

    /**
     * 驳回
     */
    function rejectCheck() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.checkState == "2") {
            toast("预约单不能驳回");
            return;
        }

        var url = '${ctx}/wms/plan/check/reject?id=' + row.id;

        $.get(url, function (data) {

            if (data == "success") {
                toast("驳回成功");
                cx();
            }

        });
    }

    /**
     * 设置预约时间
     */
    function setPlanTime() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.checkState != "1") {
            toast("请审核预约单");
            return;
        }

        d = $("#dlg").dialog({
            title: '预约',
            width: 380,
            height: 380,
            href: "${ctx}/wms/plan/form?action=add&id=" + row.id,
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

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

</script>
</body>
</html>