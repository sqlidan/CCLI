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

            <select id="state" name="state" class="easyui-combobox" data-options="width:150,prompt: '状态'">
                <option value="0">全部</option>
                <option value="1">未读</option>
                <option value="2">已读</option>
            </select>

            <input type="text" name="startTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '提醒日期起'"/>
            <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '提醒日期止'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               onclick="readMsg()">标记为已读</a>

        </form>
    </div>

</div>

<table id="dg"></table>

<script type="text/javascript">

    var dg;

    $(function () {

        dg = $('#dg').datagrid({
            method: "get",
            url: "${ctx}/wms/task/page",
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
                {field: 'content', title: '消息内容', sortable: true, width: 10},
                {
                    field: 'state', title: '消息状态', sortable: true, width: 10,
                    formatter: function (value, row, index) {
                        if (value == '0') {
                            return '未读';
                        }
                        if (value == '1') {
                            return '已读';
                        }
                    }
                },
                {field: 'createTime', title: '创建时间', sortable: true, width: 10}
            ]],
            onLoadSuccess: function (data) {

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

    });

    document.onkeydown = function () {
        if (event.keyCode == 13) cx();
    };

    //创建查询对象并查询
    function cx() {
        dg.datagrid('load', $("#searchFrom").serializeObject());
    }

    function readMsg() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.state > 0) {
            toast("消息为已读状态!");
            return;
        }

        $.ajax({
            async: false,
            type: 'POST',
            url: "${ctx}/wms/task/read",
            data: {'id': row.id},
            dataType: "text",
            success: function (msg) {

                if (msg == "success") {
                    toast("成功!");
                    cx();
                } else {
                    toast("失败!");
                }

            }
        });

    }

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

</script>
</body>
</html>