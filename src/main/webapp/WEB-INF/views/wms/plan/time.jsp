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

            <%--<input type="text" name="planCode" class="easyui-validatebox" data-options="width:120,prompt: '预约号'"/>--%>

            <%--<span class="toolbar-item dialog-tool-separator"></span>--%>
            <%--<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>--%>

            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="addForm()">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="updateForm()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               onclick="deleteForm()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>

        </form>
    </div>

</div>

<table id="dg"></table>

<div id="d"></div>

<script type="text/javascript">

    var dg;
    var d;

    document.onkeydown = function () {
        if (event.keyCode == 13) cx();
    };

    //创建查询对象并查询
    function cx() {
        dg.datagrid('load', $("#searchFrom").serializeObject());
    }

    $(function () {

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/plan/time/page',
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
                {field: 'id', title: 'id', hidden: true},
                {
                    field: 'planDate', title: '时间', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        return value == null ? "" : value.substring(0, 2) + ":" + value.substring(2, 4);
                    }
                },
                {field: 'largeNo', title: '最大数量', sortable: true, width: 100}
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
     * 设置预约时间
     */
    function addForm() {

        d = $("#d").dialog({
            title: '添加',
            width: 300,
            height: 200,
            href: '${ctx}/wms/plan/time/form?action=add',
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

    function updateForm() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        d = $("#d").dialog({
            title: '修改',
            width: 300,
            height: 200,
            href: '${ctx}/wms/plan/time/form?action=update&id=' + row.id,
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

    function deleteForm() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        var url = "${ctx}/wms/plan/time/delete?id=" + row.id;

        $.get(url, function (data) {

            if (data == "success") {
                toast("删除成功");
                cx();
            }

        });

    }

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

</script>
</body>
</html>