<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div style="padding:5px;height:auto" id="tb">
    <form id="searchFrom" action="">
<!--         <input type="text" name="filter_LIKES_itemNum" class="easyui-validatebox"
               data-options="width:150,prompt: '项号'"/> -->
        <input type="text" name="filter_LIKES_code" class="easyui-validatebox" data-options="width:150,prompt: '编码'"/>
        <input type="text" name="filter_LIKES_cargoName" class="easyui-validatebox"
               data-options="width:150,prompt: '品名'"/>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
    </form>
    <shiro:hasPermission name="base:itemname:add">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="add()">添加</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </shiro:hasPermission>
    <shiro:hasPermission name="base:itemname:delete">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           data-options="disabled:false" onclick="del()">删除</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </shiro:hasPermission>
    <shiro:hasPermission name="base:itemname:update">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
           onclick="update()">修改</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </shiro:hasPermission>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;
    $(document).keypress(function (e) {
        // 回车键事件
        if (e.which == 13) {
            gridDG();
        }
    });
    $(function () {
        gridDG();
    });

    //入库报关单列表
    function gridDG() {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/base/itemname/listjson',
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
                {field: 'id', hidden: true},
                {field: 'code', title: '商品编码', sortable: true, width: 20},
                {field: 'cargoName', title: '海关品名', sortable: true, width: 20}
                /* ,
                {field: 'cargoName', title: '品名', sortable: true, width: 20},
                {field: 'spec', title: '单位', sortable: true, width: 20} */
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    }


    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/base/itemname/delete/" + row.id,
                    success: function (data) {
                        successTip(data, dg);
                    }
                });
            }
        });
    }

    //增加
    function add() {
        d = $("#dlg").dialog({
            title: '新增',
            width: 380,
            height: 380,
            href: '${ctx}/base/itemname/create',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    if ($("#mainform").form('validate')) {
                        $("#mainform").submit();
                        d.panel('close');
                    }
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                    gridDG()
                }, 100);
            }
        });

    }

    //修改
    function update() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '修改',
            width: 380,
            height: 380,
            href: '${ctx}/base/itemname/update/' + row.id,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    if ($("#mainform").form('validate')) {
                        $("#mainform").submit();
                        d.panel('close');
                    }
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                    gridDG()
                }, 100);
            }
        });
    }


    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }


</script>
</body>
</html>