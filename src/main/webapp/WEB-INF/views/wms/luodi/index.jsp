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

            <input type="text" name="planCode" class="easyui-validatebox" data-options="width:120,prompt: '落地计划号'"/>

            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

        </form>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="addForm()">录入</a>
        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
           onclick="startTask()">激活任务</a>
        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="startElectrcity()">开始插电</a>
        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           onclick="endElectrcity()">结束插电</a>

    </div>

</div>

<table id="dg"></table>

<div id="dlg"></div>

<script type="text/javascript">

    var dg;
    var dlg;

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
            url: '${ctx}/wms/luodi/page',
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
                {field: 'luodiCode', title: '落地计划号', sortable: true, width: 100},
                {
                    field: 'startPosition', title: '历史场位', sortable: true, width: 80,
                    formatter: function (value, row, index) {
                        return value == null ? "无" : value;
                    }
                },
                {field: 'endPosition', title: '场位', sortable: true, width: 80},
                {
                    field: 'isCd', title: '是否插电', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        return value == 0 ? "否" : "是";
                    }
                },
                {
                    field: 'cdState', title: '插电状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {

                        if (row.isCd == "0") {
                            return "无";
                        }

                        if (value == "0") {
                            return "未开始";
                        } else if (value == "1") {
                            return "已开始";
                        } else if (value == "2") {
                            return "已结束";
                        }
                    }
                },
                {field: 'billCode', title: '提单号', sortable: true, width: 100},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 100},
//                {field: 'ctnVersion', title: '箱型', sortable: true, width: 100},
//                {field: 'ctnSize', title: '箱尺寸', sortable: true, width: 100},
                {field: 'clientName', title: '客户名', sortable: true, width: 150},
                {field: 'yfClientName', title: '应付客户名称', sortable: true, width: 150},
                {field: 'carNum', title: '车牌号', sortable: true, width: 80},
                {field: 'luodiTime', title: '计划时间', sortable: true, width: 150},
                {
                    field: 'luodiType', title: '操作', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == "F") {
                            return "落地";
                        } else if (value == "L") {
                            return "装车";
                        } else if (value == "M") {
                            return "场内转移";
                        }
                    }
                },
                {
                    field: 'state', title: '状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == "0") {
                            return "未执行";
                        } else if (value == "1") {
                            return "已激活";
                        } else if (value == "2") {
                            return "执行完成";
                        }
                    }
                }
//                {field: 'startTime', title: '开始时间', sortable: true, width: 100},
//                {field: 'endTime', title: '结束时间', sortable: true, width: 100},
//                {field: 'createUser', title: '创建人', sortable: true, width: 100}
            ]],
            onLoadSuccess: function (data) {

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

    });

    function addForm() {
        dlg = $("#dlg").dialog({
            title: '录入',
            width: 400,
            height: 400,
            href: '${ctx}/wms/luodi/formAdd',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {

                    var mainform = $("#mainform");

                    var url = "${ctx}/wms/luodi/form/add";

                    $("#yfClientName").val($("#yfClientId").combobox("getText"));

                    $.post(url, mainform.serializeArray(), function (data) {

                        if (data == "success") {
                            toast("录入落地计划成功");
                            dlg.panel('close');
                            cx();
                        } else {
                            toast("录入落地计划失败，" + data);
                        }

                    });

                }
            }, {
                text: '取消',
                handler: function () {
                    dlg.panel('close');
                }
            }]
        });

    }

    //修改
    function upd() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        dlg = $("#dlg").dialog({
            title: '落地计划修改',
            width: 300,
            height: 400,
            href: '${ctx}/wms/luodi/form/' + row.id,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var mainform = $("#mainform");
                    var url = "${ctx}/wms/luodi/form/update";
                    $("#clientName").val($("#clientId").combobox("getText"));
                    $.post(url, mainform.serializeArray(), function (data) {

                        if (data == "success") {
                            toast("修改落地计划成功");
                            dlg.panel('close');
                            cx();
                        } else {
                            toast("修改落地计划失败");
                        }

                    });

                }
            }, {
                text: '取消',
                handler: function () {
                    dlg.panel('close');
                }
            }]
        });
    }

    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
//        if (row.state == 2) {
        //           parent.$.messager.show({title: "提示", msg: "此入库联系单已被审核，无法删除！", position: "bottomRight"});
        //           return;
        //       }
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/wms/luodi/deleteLuoDi/" + row.id,
                    success: function (data) {
                        successTip(data, dg);
                    },
                });
            }
        });
    }

    /**
     * 开始插电
     */
    function startElectrcity() {
        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        var url = "${ctx}/wms/luodi/startElectrcity?id=" + row.id;

        $.get(url, function (data) {

            if (data == "success") {
                toast("开始插电成功");
                cx();
            } else {
                toast("开始插电失败");
            }

        });

    }

    /**
     * 结束插电
     */
    function endElectrcity() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        var url = "${ctx}/wms/luodi/endElectrcity?id=" + row.id;

        $.get(url, function (data) {

            if (data == "success") {
                toast("结束插电成功");
                cx();
            } else {
                toast("结束插电失败");
            }

        });

    }

    /**
     * 激活任务
     */
    function startTask() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        var url = "${ctx}/wms/luodi/startTask?id=" + row.id;

        $.get(url, function (data) {

            if (data == "success") {
                toast("激活任务成功");
                cx();
            } else {
                toast(data);
            }

        });

    }

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

</script>
</body>
</html>